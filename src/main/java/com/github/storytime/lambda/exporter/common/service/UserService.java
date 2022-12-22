package com.github.storytime.lambda.exporter.common.service;

import com.github.storytime.lambda.exporter.common.mapper.UserMapper;
import com.github.storytime.lambda.exporter.common.model.db.User;
import com.github.storytime.lambda.exporter.common.model.req.DynDbUserRequest;
import com.github.storytime.lambda.exporter.common.utils.TimeUtils;
import com.github.storytime.lambda.exporter.configs.Constant;
import com.github.storytime.lambda.exporter.configs.ExportConfig;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

import static java.time.Instant.now;
import static java.util.Map.of;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder;

@ApplicationScoped

public class UserService {

    @Inject
    Logger logger;
    @Inject
    DynamoDbClient dynamoDBSync;
    @Inject
    DynDbUserRequest userRequest;
    @Inject
    UserMapper userMapper;
    @Inject
    ExportConfig exportConfig;

    @NotNull(message = "User from dynamo db should not be null")
    public User findUserById(@NotBlank final String userId) {
        final var start = now();
        logger.infof("Fetching dyn db user: [%s] - started ...", userId);

        final var dbAttribute = of(Constant.DB_ID_ATTRIBUTE, builder().s(userId).build());
        final Map<String, AttributeValue> data = dynamoDBSync
                .getItem(userRequest.getRequest(exportConfig.getUserTable(), dbAttribute))
                .item();

        logger.infof("Fetching dyn db user: [%s], time: [%d] - end ...", userId, TimeUtils.timeBetween(start));
        return userMapper.userOf(data);
    }
}
