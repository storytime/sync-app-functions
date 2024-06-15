package com.github.storytime.lambda.common.mapper;


import com.github.storytime.lambda.common.model.db.DbCurrencyRate;
import com.github.storytime.lambda.common.model.db.DbUser;
import com.github.storytime.lambda.common.model.zen.TagItem;
import com.github.storytime.lambda.common.model.zen.TransactionItem;
import com.github.storytime.lambda.common.model.zen.ZenResponse;
import com.github.storytime.lambda.common.service.CurrencyService;
import com.github.storytime.lambda.common.service.DbCurrencyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jboss.logging.Logger;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.github.storytime.lambda.exporter.configs.Constant.*;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.time.LocalDate.parse;
import static java.time.ZoneId.of;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@ApplicationScoped
public class ZenCommonMapper {

    private final CurrencyService currencyService;
    private final DbCurrencyService dbCurrencyService;
    private final DateTimeFormatter yyMmDdFormatter;
    private final Logger logger;


    @Inject
    public ZenCommonMapper(final CurrencyService currencyService,
                           final DbCurrencyService dbCurrencyService,
                           @Named("yyMmDdFormatter") final DateTimeFormatter yyMmDdFormatter,
                           final Logger logger) {
        this.currencyService = currencyService;
        this.dbCurrencyService = dbCurrencyService;
        this.yyMmDdFormatter = yyMmDdFormatter;
        this.logger = logger;
    }


    private static TransactionItem reMapTag(final List<TagItem> zenTags, final TransactionItem zt, final String parentTag) {
        final var parentTagTitle = zenTags.stream()
                .filter(t -> t.getId().equalsIgnoreCase(parentTag))
                .findFirst()
                .map(tag -> ofNullable(tag.getTitle()).orElse(parentTag))
                .orElse(parentTag);
        return zt.toBuilder().tag(List.of(parentTagTitle)).build();
    }

    public static ZenResponse copyObject(final ZenResponse zenDataInRaw) {
        return ZenResponse.builder()
                .account(zenDataInRaw.getAccount())
                .country(zenDataInRaw.getCountry())
                .company(zenDataInRaw.getCompany())
                .budget(zenDataInRaw.getBudget())
                .tag(zenDataInRaw.getTag())
                .merchant(zenDataInRaw.getMerchant())
                .transaction(zenDataInRaw.getTransaction())
                .instrument(zenDataInRaw.getInstrument())
                .reminder(zenDataInRaw.getReminder())
                .reminderMarker(zenDataInRaw.getReminderMarker())
                .serverTimestamp(zenDataInRaw.getServerTimestamp())
                .user(zenDataInRaw.getUser())
                .build();
    }

    public TransactionItem flatToParentCategoryName(final List<TagItem> zenTags, final TransactionItem zt) {
        final var innerTagId = findInnerTag(zt);
        final var parentTag = findParentTagId(zenTags, innerTagId);
        return reMapTag(zenTags, zt, parentTag);
    }

    public TransactionItem flatToParentToProjectName(final List<TagItem> zenTags, final TransactionItem zt) {
        final var innerTagId = findInnerTagProject(zt);
        return reMapTag(zenTags, zt, innerTagId);
    }

    private String findParentTagId(List<TagItem> zenTags, String innerTagId) {
        return zenTags.stream()
                .filter(tagItem -> tagItem.getId().equalsIgnoreCase(innerTagId))
                .findFirst()
                .map(tagItem -> ofNullable(tagItem.getParent()).orElse(innerTagId))
                .orElse(innerTagId);
    }

    private String findInnerTag(TransactionItem zt) {
        return ofNullable(zt.getTag()).orElse(emptyList())
                .stream()
                .findFirst()
                .orElse(EMPTY);
    }

    private String findInnerTagProject(TransactionItem zt) {
        return ofNullable(zt.getTag()).orElse(emptyList())
                .stream()
                .reduce((first, second) -> second)
                .orElse(EMPTY);

    }

    public List<TransactionItem> flatTransactionToParentCategory(final List<TagItem> zenTags,
                                                                 final List<TransactionItem> trList) {
        return trList.stream()
                .map(tagItem -> flatToParentCategoryName(zenTags, tagItem))
                .toList();
    }

    public List<TransactionItem> flatTransactionProject(final List<TagItem> zenTags,
                                                        final List<TransactionItem> trList) {
        final List<TransactionItem> transactionItems = trList.stream().filter(x -> getTagFromTr(x).size() >= 2).toList();
        return transactionItems.stream()
                .map(tagItem -> flatToParentToProjectName(zenTags, tagItem))
                .toList();
    }

    private List<String> getTagFromTr(final TransactionItem x) {
        return ofNullable(x.getTag()).orElse(emptyList());
    }

    public List<TagItem> getTagsFromDiff(final ZenResponse maybeZr) {
        return Optional.of(maybeZr).flatMap(zr -> ofNullable(zr.getTag())).orElse(emptyList());
    }

    public List<TransactionItem> getZenTransactions(final ZenResponse maybeZr) {
        return Optional.of(maybeZr).flatMap(zr -> ofNullable(zr.getTransaction())).orElse(emptyList());
    }

    public ZenResponse mapToUSD(final ZenResponse zenDataFixed, final DbUser user) {
        final List<DbCurrencyRate> allRates = dbCurrencyService.getAllRates();
        final List<TransactionItem> updatedTr = zenDataFixed.getTransaction().stream()
                .map(tr -> convertIncomeOutcomeToUSD(allRates, tr, user)).toList();
        return zenDataFixed.toBuilder().transaction(updatedTr).build();
    }

    public ZenResponse mapToUAH(final ZenResponse zenDataFixed, final DbUser user) {
        final List<DbCurrencyRate> allRates = dbCurrencyService.getAllRates();
        final List<TransactionItem> updatedTr = zenDataFixed.getTransaction().stream()
                .map(tr -> convertIncomeOutcomeToUAH(allRates, tr, user)).toList();
        return zenDataFixed.toBuilder().transaction(updatedTr).build();
    }

    private TransactionItem convertIncomeOutcomeToUSD(final List<DbCurrencyRate> allRates,
                                                      final TransactionItem trList,
                                                      final DbUser user) {
        final TransactionRecord result = mapTransactionRecord(allRates, trList, user);
        final Double outcomeUsd = result.outcomeInstrument() == UAH_CURRENCY ? valueOf(result.outcomeUah()).divide(result.dbCurrencyRate().getBuyRate(), TWO_SCALE, HALF_UP).doubleValue() : result.outcomeUah();
        final Double incomeUsd = result.incomeInstrument() == UAH_CURRENCY ? valueOf(result.incomeUah()).divide(result.dbCurrencyRate().getBuyRate(), TWO_SCALE, HALF_UP).doubleValue() : result.incomeUah();
        return trList.toBuilder().outcome(outcomeUsd).income(incomeUsd).build();
    }

    private TransactionItem convertIncomeOutcomeToUAH(final List<DbCurrencyRate> allRates,
                                                      final TransactionItem trList,
                                                      final DbUser user) {
        final TransactionRecord result = mapTransactionRecord(allRates, trList, user);
        final Double outcomeUsd = result.outcomeInstrument == USD_CURRENCY ? valueOf(result.outcomeUah).multiply(result.dbCurrencyRate.getSellRate()).doubleValue() : result.outcomeUah;
        final Double incomeUsd = result.incomeInstrument == USD_CURRENCY ? valueOf(result.incomeUah).multiply(result.dbCurrencyRate.getSellRate()).doubleValue() : result.incomeUah;
        return trList.toBuilder().outcome(outcomeUsd).income(incomeUsd).build();
    }

    private TransactionRecord mapTransactionRecord(final List<DbCurrencyRate> allRates, final TransactionItem tr, final DbUser user) {
        final ZonedDateTime startDate = parse(tr.getDate(), yyMmDdFormatter).atStartOfDay(of(user.getTimeZone()));
        final DbCurrencyRate dbCurrencyRate = currencyService.findRate(PB_CASH, USD, startDate, allRates, user);
        final Double outcomeUah = tr.getOutcome();
        final int outcomeInstrument = tr.getOutcomeInstrument();
        final Double incomeUah = tr.getIncome();
        final int incomeInstrument = tr.getIncomeInstrument();
        return new TransactionRecord(dbCurrencyRate, outcomeUah, outcomeInstrument, incomeUah, incomeInstrument);
    }

    private record TransactionRecord(DbCurrencyRate dbCurrencyRate, Double outcomeUah, int outcomeInstrument,
                                     Double incomeUah, int incomeInstrument) {
    }
}
