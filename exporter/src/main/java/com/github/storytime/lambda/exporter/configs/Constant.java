package com.github.storytime.lambda.exporter.configs;

public class Constant {

    public static final Integer OUT_YEAR = 1;
    public static final Integer OUT_QUARTER = 2;
    public static final Integer OUT_MONTH = 3;
    public static final Integer IN_YEAR = 4;
    public static final Integer IN_QUARTER = 5;
    public static final Integer IN_MONTH = 6;
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final int ZERO_SCALE = 0;
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

    private Constant() {
        throw new IllegalStateException("Utility class");
    }
}
