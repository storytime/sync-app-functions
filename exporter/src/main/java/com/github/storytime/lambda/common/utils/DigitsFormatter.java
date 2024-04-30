package com.github.storytime.lambda.common.utils;

import jakarta.enterprise.context.ApplicationScoped;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static com.github.storytime.lambda.exporter.configs.Constant.*;

@ApplicationScoped
public class DigitsFormatter {

    public String formatAmount(final BigDecimal amount) {
        final String[] totalAsArray = amount.toPlainString().split(SPLITTER_EMPTY);
        final StreamEx<String> values = StreamEx.ofReversed(totalAsArray);
        final IntStreamEx indexes = IntStreamEx.range(START_INCLUSIVE, totalAsArray.length);

        final String formattedTotal = values.zipWith(indexes)
                .map(z -> z.getValue() % FORMATTER_SPLITTER == ZERO ? SPACE + z.getKey() : z.getKey())
                .collect(Collectors.joining());

        return new StringBuilder(formattedTotal.trim()).reverse().toString();
    }
}
