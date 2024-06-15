package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.db.DbCurrencyRate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.List;

import static com.github.storytime.lambda.common.utils.TimeUtils.timeBetween;
import static java.time.Instant.now;

@ApplicationScoped
public class DbCurrencyService {

    private final Logger logger;
    private final DynamoDbTable<DbCurrencyRate> dbCurrencyRateDynamoDbTable;

    @Inject
    public DbCurrencyService(final Logger logger,
                             final DynamoDbTable<DbCurrencyRate> dbCurrencyRateDynamoDbTable) {
        this.logger = logger;
        this.dbCurrencyRateDynamoDbTable = dbCurrencyRateDynamoDbTable;
    }

    public List<DbCurrencyRate> getAllRates() {
        final var start = now();
        logger.infof("Fetching all rates - started ...");
        final List<DbCurrencyRate> dbCurrencyRates = dbCurrencyRateDynamoDbTable.scan().items().stream().toList();
        logger.infof("Fetching fetching rates, time: [%d] - end ...", timeBetween(start));
        return dbCurrencyRates;
    }

    public DbCurrencyRate saveRate(DbCurrencyRate rate) {
        final var start = now();
        logger.infof("Saving rate in DB for date: [%s]... ", rate.getHumanDate());
        final DbCurrencyRate dbCurrencyRate = dbCurrencyRateDynamoDbTable.updateItem(rate);
        logger.infof("Saving rate in DB - done,  date: [%s], time: [%d] - end ...", rate.getHumanDate(), timeBetween(start));
        return dbCurrencyRate;
    }
}
