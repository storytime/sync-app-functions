package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.db.DbCurrencyRate;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

import static com.github.storytime.lambda.common.utils.TimeUtils.timeBetween;
import static java.time.Instant.now;

@ApplicationScoped
public class DbCurrencyService {

    @Inject
    Logger logger;

    @Inject
    DynamoDbTable<DbCurrencyRate> dbCurrencyRateDynamoDbTable;


    public List<DbCurrencyRate> getAllRates() {
        final var start = now();
        logger.infof("Fetching all rates - started ...");
        List<DbCurrencyRate> dbCurrencyRates = dbCurrencyRateDynamoDbTable.scan().items().stream().toList();
        logger.infof("Fetching fetching rates, time: [%d] - end ...", timeBetween(start));
        return dbCurrencyRates;
    }

    public DbCurrencyRate saveRate(DbCurrencyRate rate) {
        final var start = now();
        logger.infof("Saving rate in DB...");
        DbCurrencyRate dbCurrencyRate = dbCurrencyRateDynamoDbTable.updateItem(rate);
        logger.infof("Saving rate in DB - done, time: [%d] - end ...", timeBetween(start));
        return dbCurrencyRate;
    }
}
