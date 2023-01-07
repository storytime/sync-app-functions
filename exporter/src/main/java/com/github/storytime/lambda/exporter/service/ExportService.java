package com.github.storytime.lambda.exporter.service;


import com.github.storytime.lambda.common.model.zen.TransactionItem;
import com.github.storytime.lambda.common.model.zen.ZenResponse;
import com.github.storytime.lambda.exporter.configs.Constant;
import com.github.storytime.lambda.exporter.mapper.ExportMapper;
import com.github.storytime.lambda.exporter.model.ExportTransaction;
import org.jboss.logging.Logger;

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
    // TransactionItem.date - are taking
    //
    private final Function<TransactionItem, ExportTransaction> outMonthlyDateMapperFk = t -> new ExportTransaction(t.getOutcome(), getCategory(t), getYear(t) + DATE_SEPARATOR + getMonth(t));
    //TODO move getOutcome//getIncome to function
    private final Function<TransactionItem, ExportTransaction> inMonthlyDateMapperFk = t -> new ExportTransaction(t.getIncome(), getCategory(t), getYear(t) + DATE_SEPARATOR + getMonth(t));
    private final Function<TransactionItem, ExportTransaction> outYearlyDateMapperFk = t -> new ExportTransaction(t.getOutcome(), getCategory(t), YEAR + getYear(t));
    private final Function<TransactionItem, ExportTransaction> inYearlyDateMapperFk = t -> new ExportTransaction(t.getIncome(), getCategory(t), YEAR + getYear(t));
    private final Function<TransactionItem, ExportTransaction> outQuarterlyDateMapperFk = t -> new ExportTransaction(t.getOutcome(), getCategory(t), QUARTER + getYear(t) + DATE_SEPARATOR + quarter.get(getMonth(t)));
    private final Function<TransactionItem, ExportTransaction> inQuarterlyDateMapperFk = t -> new ExportTransaction(t.getIncome(), getCategory(t), QUARTER + getYear(t) + DATE_SEPARATOR + quarter.get(getMonth(t)));
    private final Predicate<TransactionItem> transactionOutSelectPredicate = t -> t.getIncome().equals(INITIAL_VALUE) && !t.getOutcome().equals(INITIAL_VALUE);
    private final Predicate<TransactionItem> transactionInSelectPredicate = t -> t.getOutcome().equals(INITIAL_VALUE) && !t.getIncome().equals(INITIAL_VALUE);
    @Inject
    Logger logger;
    @Inject
    ExportMapper exportMapper;

    public ExportService() {
        quarter.put(JAN, Q1);
        quarter.put(FEB, Q1);
        quarter.put(MAR, Q1);
        quarter.put(APR, Constant.Q2);
        quarter.put(Constant.MAY, Constant.Q2);
        quarter.put(Constant.JUN, Constant.Q2);
        quarter.put(Constant.JUL, Constant.Q3);
        quarter.put(Constant.AUG, Constant.Q3);
        quarter.put(Constant.SEP, Constant.Q3);
        quarter.put(Constant.OCT, Constant.Q4);
        quarter.put(Constant.NOV, Constant.Q4);
        quarter.put(Constant.DEC, Constant.Q4);
    }

    public List<Map<String, String>> getOutQuarterlyData(final ZenResponse zenDiff) {
        return getExportData(zenDiff, outQuarterlyDateMapperFk, transactionOutSelectPredicate);
    }

    public List<Map<String, String>> getInQuarterData(final ZenResponse zenDiff) {
        return getExportData(zenDiff, inQuarterlyDateMapperFk, transactionInSelectPredicate);
    }

    public List<Map<String, String>> getInYearlyData(final ZenResponse zenDiff) {
        return getExportData(zenDiff, inYearlyDateMapperFk, transactionInSelectPredicate);
    }


    public List<Map<String, String>> getOutYearlyData(final ZenResponse zenDiff) {
        return getExportData(zenDiff, outYearlyDateMapperFk, transactionOutSelectPredicate);
    }

    public List<Map<String, String>> getInMonthlyData(final ZenResponse zenDiff) {
        return getExportData(zenDiff, inMonthlyDateMapperFk, transactionInSelectPredicate);
    }

    public List<Map<String, String>> getOutMonthlyData(final ZenResponse zenDiff) {
        return getExportData(zenDiff, outMonthlyDateMapperFk, transactionOutSelectPredicate);
    }

    private List<Map<String, String>> getExportData(final ZenResponse zenDiff,
                                                    final Function<TransactionItem, ExportTransaction> transactionMapperByPeriod,
                                                    final Predicate<TransactionItem> transactionInOutFilter) {


        final List<ExportTransaction> exportTransactions = exportMapper.mapTransaction(transactionMapperByPeriod, transactionInOutFilter, zenDiff);

        final LinkedHashMap<String, List<ExportTransaction>> collect = exportTransactions.stream()
                .collect(groupingBy(ExportTransaction::category, toList()))
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new))
                .entrySet()
                .stream()
                .sorted(comparingByKey())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new));

        return exportMapper.mapExportData(collect);
    }
}