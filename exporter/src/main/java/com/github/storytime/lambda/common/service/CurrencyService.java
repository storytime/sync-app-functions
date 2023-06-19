package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.db.DbCurrencyRate;
import com.github.storytime.lambda.common.model.db.DbUser;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.github.storytime.lambda.exporter.configs.Constant.*;
import static java.lang.String.valueOf;
import static java.time.Instant.ofEpochSecond;
import static java.time.LocalTime.MIN;
import static java.time.ZoneId.of;
import static java.time.ZonedDateTime.ofInstant;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;

@ApplicationScoped
public class CurrencyService {

    @Inject
    DbCurrencyService dbCurrencyService;

    @Inject
    @Named("isoFormatter")
    DateTimeFormatter isoFormatter;

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
                                   final List<DbCurrencyRate> allRates,
                                   final DbUser user) {

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
                        .map(x -> buildRate(source, beggingOfTheDay, type, x.getPurchaseRate(), x.getSaleRate(), user))
                        .map(x -> dbCurrencyService.saveRate(x)).findFirst())
                .stream()
                .findFirst()
                .orElseThrow();
    }

    private DbCurrencyRate buildRate(final String cs,
                                     final long date,
                                     final String currencyType,
                                     final BigDecimal sellPrate,
                                     final BigDecimal buyPrate,
                                     final DbUser user) {
        return DbCurrencyRate.builder()
                .id(randomUUID().toString())
                .currencySource(cs)
                .currencyType(currencyType)
                .dateTime(date)
                .humanDate(isoFormatter.format(ofInstant(ofEpochSecond(date), of(user.getTimeZone()))))
                .buyRate(buyPrate)
                .sellRate(sellPrate)
                .build();
    }

    private boolean isEq(final String baseCcy, final String currency) {
        return ofNullable(baseCcy).orElse(EMPTY).equalsIgnoreCase(currency);
    }
}
