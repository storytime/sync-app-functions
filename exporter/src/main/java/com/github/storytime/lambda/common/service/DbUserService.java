package com.github.storytime.lambda.common.service;

import com.github.storytime.lambda.common.model.db.DbUser;
import com.github.storytime.lambda.common.utils.TimeUtils;
import io.smallrye.common.constraint.NotNull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import static java.time.Instant.now;

@ApplicationScoped

public class DbUserService {

    private final Logger logger;
    private final DynamoDbTable<DbUser> userTable;

    @Inject
    public DbUserService(final Logger logger,
                         final DynamoDbTable<DbUser> userTable) {
        this.logger = logger;
        this.userTable = userTable;
    }

    @NotNull
    public DbUser findUserById(@NotNull final String userId) {
        final var start = now();
        logger.infof("Fetching dyn db user: [%s] - started ...", userId);
        DbUser user = userTable.getItem(Key.builder().partitionValue(userId).build());
        logger.infof("Fetching dyn db user: [%s], time: [%d] - end ...", userId, TimeUtils.timeBetween(start));
        return user;
    }
}
