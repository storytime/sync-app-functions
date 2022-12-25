package com.github.storytime.lambda.api.configs;


import com.github.storytime.lambda.common.model.db.DbExport;
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
    ExportApiConfig exportApiConfig;

    static final TableSchema<DbExport> EXPORT_TABLE_SCHEMA = TableSchema.fromClass(DbExport.class);

    @Produces
    @DefaultBean
    public DynamoDbEnhancedClient dynamoDbEnhancedClientCustom() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDBSync)
                .build();
    }

    @Produces
    @DefaultBean
    public DynamoDbTable<DbExport> exportTable(DynamoDbEnhancedClient dynamoDbEnhancedClientCustom) {
        return dynamoDbEnhancedClientCustom.table(exportApiConfig.getExportTable(), EXPORT_TABLE_SCHEMA);
    }

}
