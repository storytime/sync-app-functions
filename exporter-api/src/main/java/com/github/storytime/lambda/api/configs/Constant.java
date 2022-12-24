package com.github.storytime.lambda.api.configs;

public class Constant {

    public static final String DB_USER_ID_ATTRIBUTE = "userId";
    public static final String TYPE = "type";

    public static final int OUT_YEAR = 1;
    public static final int OUT_QUARTER = 2;
    public static final int OUT_MONTH = 3;
    public static final int IN_YEAR = 4;
    public static final int IN_QUARTER = 5;
    public static final int IN_MONTH = 6;

    private Constant() {
        throw new IllegalStateException("Utility class");
    }
}
