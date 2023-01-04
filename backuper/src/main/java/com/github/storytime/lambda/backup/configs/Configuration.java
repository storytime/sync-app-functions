package com.github.storytime.lambda.backup.configs;

import com.github.storytime.lambda.common.model.db.DbUser;
import io.quarkus.arc.DefaultBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@Dependent
public class Configuration {
    @Inject
    DynamoDbClient dynamoDBSync;

    @Inject
    BackupConfig exportConfig;

    static final TableSchema<DbUser> USER_TABLE_SCHEMA = TableSchema.fromClass(DbUser.class);

    @Produces
    @DefaultBean
    public DynamoDbEnhancedClient dynamoDbEnhancedClientCustom() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDBSync)
                .build();
    }

    @Produces
    public DynamoDbTable<DbUser> userTable(DynamoDbEnhancedClient dynamoDbEnhancedClientCustom) {
        return dynamoDbEnhancedClientCustom.table(exportConfig.getUserTable(), USER_TABLE_SCHEMA);
    }

}