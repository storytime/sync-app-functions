package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.db.DbUser;
import com.github.storytime.lambda.common.utils.TimeUtils;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static java.time.Instant.now;

@ApplicationScoped

public class DbUserService {

    @Inject
    Logger logger;

    @Inject
    DynamoDbTable<DbUser> userTable;

    @NotNull(message = "User from dynamo db should not be null")
    public DbUser findUserById(@NotBlank final String userId) {
        final var start = now();
        logger.infof("Fetching dyn db user: [%s] - started ...", userId);
        DbUser user = userTable.getItem(Key.builder().partitionValue(userId).build());
        logger.infof("Fetching dyn db user: [%s], time: [%d] - end ...", userId, TimeUtils.timeBetween(start));
        return user;
    }
}
