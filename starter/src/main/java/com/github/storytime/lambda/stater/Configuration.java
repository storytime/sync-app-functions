package com.github.storytime.lambda.stater;


import com.github.storytime.lambda.common.model.db.DbUser;
import io.quarkus.arc.DefaultBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@Dependent
public class Configuration {
    static final TableSchema<DbUser> USER_TABLE_SCHEMA = TableSchema.fromClass(DbUser.class);

    final DynamoDbClient dynamoDBSync;
    final StarterConfig starterConfig;

    @Inject
    public Configuration(final DynamoDbClient dynamoDBSync,
                         final StarterConfig starterConfig) {
        this.dynamoDBSync = dynamoDBSync;
        this.starterConfig = starterConfig;
    }

    @Produces
    public DynamoDbEnhancedClient dynamoDbEnhancedClientCustom() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDBSync)
                .build();
    }

    @Produces
    @DefaultBean
    public DynamoDbTable<DbUser> userTable(DynamoDbEnhancedClient dynamoDbEnhancedClientCustom) {
        return dynamoDbEnhancedClientCustom.table(starterConfig.getUserTable(), USER_TABLE_SCHEMA);
    }
}
