package com.github.storytime.lambda.api.configs;


import com.github.storytime.lambda.common.model.db.DbExport;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Dependent
public class Configuration {


    final DynamoDbClient dynamoDBSync;
    final ExportApiConfig exportApiConfig;

    @Inject
    public Configuration(final DynamoDbClient dynamoDBSync,
                         final ExportApiConfig exportApiConfig) {
        this.dynamoDBSync = dynamoDBSync;
        this.exportApiConfig = exportApiConfig;
    }

    static final TableSchema<DbExport> EXPORT_TABLE_SCHEMA = TableSchema.fromClass(DbExport.class);

    @Produces
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
