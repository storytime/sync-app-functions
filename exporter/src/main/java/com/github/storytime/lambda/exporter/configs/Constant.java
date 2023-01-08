package com.github.storytime.lambda.exporter.configs;

public class Constant {

    public static final int OUT_YEAR_UAH = 1;
    public static final int OUT_QUARTER_UAH = 2;
    public static final int OUT_MONTH_UAH = 3;
    public static final int IN_YEAR_UAH = 4;
    public static final int IN_QUARTER_UAH = 5;
    public static final int IN_MONTH_UAH = 6;

    public static final int OUT_YEAR_USD = 7;
    public static final int OUT_QUARTER_USD = 8;
    public static final int OUT_MONTH_USD = 9;
    public static final int IN_YEAR_USD = 10;
    public static final int IN_QUARTER_USD = 11;
    public static final int IN_MONTH_USD = 12;

    public static final String UAH_STR = "UAH";

    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final int ZERO_SCALE = 0;
    public static final int TWO_SCALE = 2;
    public static final String PROJECT_TAG = "#";
    public static final String SPLITTER_EMPTY = "(?<=.)";
    public static final int START_INCLUSIVE = 0;
    public static final int ZERO = 0;
    public static final int FORMATTER_SPLITTER = 3;
    public static final String CATEGORY = "Category";
    public static final double INITIAL_VALUE = 0D;
    public static final String JUN = "06";
    public static final String TOTAL_EXPORT = "Total";
    public static final String MEDIAN = "Median";
    public static final String AVG_VALUED = "Averg";
    public static final int YEAR_INDEX_BEGIN = 2;
    public static final int YEAR_END_INDEX = 4;
    public static final String DATE_SEPARATOR = "-";
    public static final String YEAR = "Y ";
    public static final String QUARTER = "Q ";
    public static final int MONTH_BEGIN_INDEX = 5;
    public static final int MONTH_END_INDEX = 7;
    public static final String JAN = "01";
    public static final String FEB = "02";
    public static final String APR = "04";
    public static final String MAR = "03";
    public static final String MAY = "05";
    public static final String JUL = "07";
    public static final String AUG = "08";
    public static final String SEP = "09";
    public static final String OCT = "10";
    public static final String NOV = "11";
    public static final String DEC = "12";
    public static final String Q1 = "1";
    public static final String Q2 = "2";
    public static final String Q3 = "3";
    public static final String Q4 = "4";
    public static final String BEARER = "Bearer";
    public static final int TEN_ADJUSTMENT = 10;
    public static final String DOT = ".";
    public static final String ZERO_ADJUSTMENT = "0";
    public static final String PB_CASH = "PB_CASH";
    public static final String USD = "USD";
    public static final String DD_MM_YYYY_HH_MM_SS_SSS = "dd-MM-yyyy - HH:mm:ss.SSS";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";


    private Constant() {
        throw new IllegalStateException("Utility class");
    }
}
