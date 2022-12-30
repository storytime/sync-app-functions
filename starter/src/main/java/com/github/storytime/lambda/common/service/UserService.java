package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.db.DbUser;
import com.github.storytime.lambda.common.utils.TimeUtils;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static java.time.Instant.now;

@ApplicationScoped

public class UserService {

    @Inject
    Logger logger;

    @Inject
    DynamoDbTable<DbUser> userTable;

    public PageIterable<DbUser> findAll() {
        final var start = now();
        logger.infof("Fetching all db user: [%s] - started ...");
        PageIterable<DbUser> users = userTable.scan();
        logger.infof("Fetching all db time: [%d] - end ...", TimeUtils.timeBetween(start));
        return users;
    }
}
