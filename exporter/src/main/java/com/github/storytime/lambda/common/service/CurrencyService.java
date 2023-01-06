package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.db.DbCurrencyRate;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.github.storytime.lambda.exporter.configs.Constant.*;
import static java.lang.String.valueOf;
import static java.time.LocalTime.MIN;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@ApplicationScoped
public class CurrencyService {

    @Inject
    DbCurrencyService dbCurrencyService;

    @Inject
    @RestClient
    PBCurrencyClientService pbCurrencyClientService;

    private static String dateFoPbReq(final ZonedDateTime startDate) {
        final String day = getAdjustedString(startDate.getDayOfMonth());
        final String mo = getAdjustedString(startDate.getMonth().getValue());
        return day + DOT + mo + DOT + startDate.getYear();
    }

    private static String getAdjustedString(int dayOfMonth) {
        return dayOfMonth < TEN_ADJUSTMENT ? ZERO_ADJUSTMENT + dayOfMonth : valueOf(dayOfMonth);
    }

    public DbCurrencyRate findRate(final String source,
                                   final String type,
                                   final ZonedDateTime startDate,
                                   final List<DbCurrencyRate> allRates) {

        final String dateFoPbReq = dateFoPbReq(startDate);
        final long beggingOfTheDay = startDate.with(MIN).toInstant().getEpochSecond();
        return allRates.stream()
                .filter(x -> x.getDateTime().equals(beggingOfTheDay))
                .toList().stream()
                .filter(x -> x.getCurrencyType().equalsIgnoreCase(type) && x.getCurrencySource().equalsIgnoreCase(source))
                .findFirst()
                .or(() -> ofNullable(pbCurrencyClientService.getRates(dateFoPbReq).getExchangeRate())
                        .orElse(emptyList())
                        .stream().filter(cr -> isEq(cr.getBaseCurrency(), UAH_STR) && isEq(cr.getCurrency(), type)).findFirst()
                        .stream()
                        .map(x -> buildRate(source, beggingOfTheDay, type, x.getPurchaseRate(), x.getSaleRate()))
                        .map(x -> dbCurrencyService.saveRate(x)).findFirst())
                .stream()
                .findFirst()
                .orElseThrow();
    }

    private DbCurrencyRate buildRate(final String cs,
                                     final long date,
                                     final String currencyType,
                                     final BigDecimal sellPrate,
                                     final BigDecimal buyPrate) {
        return DbCurrencyRate.builder()
                .id(UUID.randomUUID().toString())
                .currencySource(cs)
                .currencyType(currencyType)
                .dateTime(date)
                .buyRate(buyPrate)
                .sellRate(sellPrate)
                .build();
    }

    private boolean isEq(final String baseCcy, final String currency) {
        return ofNullable(baseCcy).orElse(EMPTY).equalsIgnoreCase(currency);
    }
}
