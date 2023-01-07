package com.github.storytime.lambda.common.mapper;


import com.github.storytime.lambda.common.model.db.DbCurrencyRate;
import com.github.storytime.lambda.common.model.zen.AccountItem;
import com.github.storytime.lambda.common.model.zen.TagItem;
import com.github.storytime.lambda.common.model.zen.TransactionItem;
import com.github.storytime.lambda.common.model.zen.ZenResponse;
import com.github.storytime.lambda.common.service.CurrencyService;
import com.github.storytime.lambda.common.service.DbCurrencyService;
import com.github.storytime.lambda.exporter.configs.Constant;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.storytime.lambda.exporter.configs.Constant.*;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.time.ZoneId.of;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingLong;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;

@ApplicationScoped
public class ZenCommonMapper {

    @Inject
    CurrencyService currencyService;
    @Inject
    DbCurrencyService dbCurrencyService;


    public TransactionItem flatToParentCategoryId(final List<TagItem> zenTags, final TransactionItem zt) {
        final String innerTagId = findInnerTag(zt);
        final String parentTag = findParentTagId(zenTags, innerTagId);
        return zt.toBuilder().tag(List.of(parentTag)).build();
    }

    public TransactionItem flatToParentCategoryName(final List<TagItem> zenTags, final TransactionItem zt) {
        final var innerTagId = findInnerTag(zt);
        final var parentTag = findParentTagId(zenTags, innerTagId);
        final var parentTagTitle = zenTags.stream().filter(t -> t.getId().equalsIgnoreCase(parentTag)).findFirst().map(tag -> ofNullable(tag.getTitle()).orElse(parentTag)).orElse(parentTag);
        return zt.toBuilder().tag(List.of(parentTagTitle)).build();
    }

    private String findParentTagId(List<TagItem> zenTags, String innerTagId) {
        return zenTags.stream().filter(tagItem -> tagItem.getId().equalsIgnoreCase(innerTagId)).findFirst().map(tagItem -> ofNullable(tagItem.getParent()).orElse(innerTagId)).orElse(innerTagId);
    }

    private String findInnerTag(TransactionItem zt) {
        return ofNullable(zt.getTag()).orElse(emptyList()).stream().filter(not(s -> s.startsWith(Constant.PROJECT_TAG))).findFirst().orElse(Constant.EMPTY);
    }

    public List<TransactionItem> flatToParentCategoryTransactionList(final List<TagItem> zenTags, final List<TransactionItem> trList) {
        return trList.stream().map(tagItem -> flatToParentCategoryName(zenTags, tagItem)).toList();
    }

    public String getTagNameByTagId(final List<TagItem> zenTags, final String id) {
        return zenTags.stream().filter(tagItem -> tagItem.getId().equalsIgnoreCase(id)).map(TagItem::getTitle).findFirst().orElse(Constant.EMPTY);
    }

    public List<TagItem> getTags(final ZenResponse maybeZr) {
        return Optional.of(maybeZr).flatMap(zr -> ofNullable(zr.getTag())).orElse(emptyList());
    }

    public List<AccountItem> getZenAccounts(final ZenResponse maybeZr) {
        return of(maybeZr).flatMap(zr -> ofNullable(zr.getAccount())).orElse(emptyList());
    }

    public List<TransactionItem> getZenTransactions(final ZenResponse maybeZr) {
        return Optional.of(maybeZr).flatMap(zr -> ofNullable(zr.getTransaction())).orElse(emptyList());
    }

    public Map<String, BigDecimal> getZenTagsSummaryByCategory(long startDate, long endDate, final ZenResponse maybeZr) {

        final List<TransactionItem> transactionItems = this.getZenTransactions(maybeZr);
        final List<TagItem> zenTags = this.getTags(maybeZr).stream().filter(not(t -> t.getTitle().startsWith(Constant.PROJECT_TAG))).toList();

        final var zenTr = transactionItems.stream().filter(not(TransactionItem::isDeleted)).filter(zTr -> zTr.getCreated() >= startDate && zTr.getCreated() < endDate).toList().stream().map(zt -> this.flatToParentCategoryId(zenTags, zt)).sorted(comparing(TransactionItem::getCreated)).toList();

        //TODO: add amount of transactions
        final Map<String, DoubleSummaryStatistics> groupByTags = zenTr.stream().collect(groupingBy(transactionItem -> transactionItem.getTag().stream().findFirst().orElse(Constant.EMPTY), Collectors.summarizingDouble(TransactionItem::getOutcome)));

        final TreeMap<String, BigDecimal> zenSummary = new TreeMap<>();
        groupByTags.forEach((zenTagId, summary) -> zenSummary.put(this.getTagNameByTagId(zenTags, zenTagId), valueOf(summary.getSum())));

        return zenSummary;
    }


    public ZenResponse mapToUSD(final ZenResponse zenDataFixed) {
        final List<DbCurrencyRate> allRates = dbCurrencyService.getAllRates();
        final List<TransactionItem> updatedTr = zenDataFixed.getTransaction().stream()
                .map(tr -> convertIncomeOutcomeToUSD(allRates, tr)).toList();
        return zenDataFixed.toBuilder().transaction(updatedTr).build();
    }

    private TransactionItem convertIncomeOutcomeToUSD(final List<DbCurrencyRate> allRates, final TransactionItem tr) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        ZonedDateTime startDate = LocalDate.parse(tr.getDate(), dtf).atStartOfDay(of(EUROPE_KIEV));

        final DbCurrencyRate dbCurrencyRate = currencyService.findRate(PB_CASH, USD, startDate, allRates);
        final Double outcomeUah = tr.getOutcome();
        final Double incomeUah = tr.getIncome();
        final Double outcomeUsd = valueOf(outcomeUah).divide(dbCurrencyRate.getBuyRate(), TWO_SCALE, HALF_UP).doubleValue();
        final Double incomeUsd = valueOf(incomeUah).divide(dbCurrencyRate.getBuyRate(), TWO_SCALE, HALF_UP).doubleValue();
        return tr.toBuilder().outcome(outcomeUsd).income(incomeUsd).build();
    }
}
