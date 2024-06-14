package com.github.storytime.lambda.backup.configs;

import com.github.storytime.lambda.common.model.db.DbUser;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Dependent
public class Configuration {
    static final TableSchema<DbUser> USER_TABLE_SCHEMA = TableSchema.fromClass(DbUser.class);

    private final DynamoDbClient dynamoDBSync;
    private final BackupConfig exportConfig;

    @Inject
    public Configuration(final DynamoDbClient dynamoDBSync,
                         final BackupConfig exportConfig) {
        this.dynamoDBSync = dynamoDBSync;
        this.exportConfig = exportConfig;
    }

    @Produces
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
