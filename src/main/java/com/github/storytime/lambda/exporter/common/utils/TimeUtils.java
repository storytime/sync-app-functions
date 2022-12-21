package com.github.storytime.lambda.exporter.common.utils;

import java.time.Instant;

import static java.time.Duration.between;
import static java.time.Instant.now;

public class TimeUtils {

    private TimeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static long timeBetween(final Instant lambdaStart) {
        return between(lambdaStart, now()).toMillis();
    }
}
