package com.github.storytime.lambda.api.configs;

public class Constant {

    public static final String DB_USER_ID_ATTRIBUTE = "userId";
    public static final String TYPE = "type";

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

    private Constant() {
        throw new IllegalStateException("Utility class");
    }
}
