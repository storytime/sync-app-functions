package com.github.storytime.lambda.exporter.common.service;

import com.github.storytime.lambda.exporter.common.utils.TimeUtils;
import com.github.storytime.lambda.exporter.common.model.db.DbUser;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static java.time.Instant.now;
import static java.util.Map.of;

@ApplicationScoped

public class UserService {

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
