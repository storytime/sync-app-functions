package com.github.storytime.lambda.exporter.service;


import com.github.storytime.lambda.common.model.zen.TransactionItem;
import com.github.storytime.lambda.common.model.zen.ZenResponse;
import com.github.storytime.lambda.exporter.mapper.ExportMapper;
import com.github.storytime.lambda.exporter.model.ExportTransaction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.storytime.lambda.exporter.configs.Constant.*;
import static com.github.storytime.lambda.exporter.mapper.ExportMapper.*;
import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.*;


@ApplicationScoped
public class ExportService {

    private static final Map<String, String> quarter = new TreeMap<>();

    //**
    // TransactionItem.date - is taking
    //
    private final Function<TransactionItem, ExportTransaction> outMonthlyDateMapperFk = t -> new ExportTransaction(t.getId(), t.getOutcome(), getCategory(t), getYear(t) + DATE_SEPARATOR + getMonth(t));
    //TODO move getOutcome//getIncome to function
    private final Function<TransactionItem, ExportTransaction> inMonthlyDateMapperFk = t -> new ExportTransaction(t.getId(), t.getIncome(), getCategory(t), getYear(t) + DATE_SEPARATOR + getMonth(t));
    private final Function<TransactionItem, ExportTransaction> outYearlyDateMapperFk = t -> new ExportTransaction(t.getId(), t.getOutcome(), getCategory(t), YEAR + getYear(t));
    private final Function<TransactionItem, ExportTransaction> inYearlyDateMapperFk = t -> new ExportTransaction(t.getId(), t.getIncome(), getCategory(t), YEAR + getYear(t));
    private final Function<TransactionItem, ExportTransaction> outQuarterlyDateMapperFk = t -> new ExportTransaction(t.getId(), t.getOutcome(), getCategory(t), QUARTER + getYear(t) + DATE_SEPARATOR + quarter.get(getMonth(t)));
    private final Function<TransactionItem, ExportTransaction> inQuarterlyDateMapperFk = t -> new ExportTransaction(t.getId(), t.getIncome(), getCategory(t), QUARTER + getYear(t) + DATE_SEPARATOR + quarter.get(getMonth(t)));
    private final Predicate<TransactionItem> transactionOutSelectPredicate = t -> t.getIncome().equals(INITIAL_VALUE) && !t.getOutcome().equals(INITIAL_VALUE);
    private final Predicate<TransactionItem> transactionInSelectPredicate = t -> t.getOutcome().equals(INITIAL_VALUE) && !t.getIncome().equals(INITIAL_VALUE);
    @Inject
    ExportMapper exportMapper;

    public ExportService() {
        quarter.put(JAN, Q1);
        quarter.put(FEB, Q1);
        quarter.put(MAR, Q1);
        quarter.put(APR, Q2);
        quarter.put(MAY, Q2);
        quarter.put(JUN, Q2);
        quarter.put(JUL, Q3);
        quarter.put(AUG, Q3);
        quarter.put(SEP, Q3);
        quarter.put(OCT, Q4);
        quarter.put(NOV, Q4);
        quarter.put(DEC, Q4);
    }

    public List<Map<String, String>> getOutQuarterlyDataByCategory(final ZenResponse zenDiff) {
        return getExportDataForCategory(zenDiff, outQuarterlyDateMapperFk, transactionOutSelectPredicate);
    }

    public List<Map<String, String>> getInQuarterDataByCategory(final ZenResponse zenDiff) {
        return getExportDataForCategory(zenDiff, inQuarterlyDateMapperFk, transactionInSelectPredicate);
    }

    public List<Map<String, String>> getInYearlyDataByCategory(final ZenResponse zenDiff) {
        return getExportDataForCategory(zenDiff, inYearlyDateMapperFk, transactionInSelectPredicate);
    }

    public List<Map<String, String>> getOutYearlyDataByCategory(final ZenResponse zenDiff) {
        return getExportDataForCategory(zenDiff, outYearlyDateMapperFk, transactionOutSelectPredicate);
    }

    public List<Map<String, String>> getInMonthlyDataByCategory(final ZenResponse zenDiff) {
        return getExportDataForCategory(zenDiff, inMonthlyDateMapperFk, transactionInSelectPredicate);
    }

    public List<Map<String, String>> getOutMonthlyDataByCategory(final ZenResponse zenDiff) {
        return getExportDataForCategory(zenDiff, outMonthlyDateMapperFk, transactionOutSelectPredicate);
    }

    public List<Map<String, String>> getOutYearlyDataByProject(final ZenResponse zenDiff) {
        return getExportDataByProject(zenDiff, outYearlyDateMapperFk, transactionOutSelectPredicate);
    }

    public List<Map<String, String>> getInYearlyDataByProject(final ZenResponse zenDiff) {
        return getExportDataByProject(zenDiff, inYearlyDateMapperFk, transactionInSelectPredicate);
    }

    private List<Map<String, String>> getExportDataForCategory(final ZenResponse zenDiff,
                                                               final Function<TransactionItem, ExportTransaction> transactionMapperByPeriod,
                                                               final Predicate<TransactionItem> transactionInOutFilter) {

        final List<ExportTransaction> exportTransactionsByCategory = exportMapper.mapByCategory(transactionMapperByPeriod, transactionInOutFilter, zenDiff);
        final LinkedHashMap<String, List<ExportTransaction>> byCategory = groupDataForMapper(exportTransactionsByCategory);
        return exportMapper.mapCategoryExportData(byCategory);
    }

    private List<Map<String, String>> getExportDataByProject(final ZenResponse zenDiff,
                                                             final Function<TransactionItem, ExportTransaction> transactionMapperByPeriod,
                                                             final Predicate<TransactionItem> transactionInOutFilter) {

        final List<ExportTransaction> exportTransactionsByProject = exportMapper.mapByProject(transactionMapperByPeriod, transactionInOutFilter, zenDiff);
        final LinkedHashMap<String, List<ExportTransaction>> byCategory = groupDataForMapper(exportTransactionsByProject);
        return exportMapper.mapProjectExportData(byCategory);
    }

    private LinkedHashMap<String, List<ExportTransaction>> groupDataForMapper(final List<ExportTransaction> groupData) {
        return groupData.stream()
                .collect(groupingBy(ExportTransaction::category, toList()))
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new))
                .entrySet()
                .stream()
                .sorted(comparingByKey())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new));
    }
}