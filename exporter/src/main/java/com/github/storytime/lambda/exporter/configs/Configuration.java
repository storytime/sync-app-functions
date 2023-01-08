package com.github.storytime.lambda.exporter.configs;

import com.github.storytime.lambda.common.model.db.DbCurrencyRate;
import com.github.storytime.lambda.common.model.db.DbExport;
import com.github.storytime.lambda.common.model.db.DbUser;
import io.quarkus.arc.DefaultBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.format.DateTimeFormatter;

import static com.github.storytime.lambda.exporter.configs.Constant.DD_MM_YYYY_HH_MM_SS_SSS;
import static com.github.storytime.lambda.exporter.configs.Constant.YYYY_MM_DD;

@Dependent
public class Configuration {
    static final TableSchema<DbExport> EXPORT_TABLE_SCHEMA = TableSchema.fromClass(DbExport.class);
    static final TableSchema<DbUser> USER_TABLE_SCHEMA = TableSchema.fromClass(DbUser.class);
    static final TableSchema<DbCurrencyRate> CURRENCY_TABLE_SCHEMA = TableSchema.fromClass(DbCurrencyRate.class);
    @Inject
    DynamoDbClient dynamoDBSync;
    @Inject
    ExportConfig exportConfig;

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
        return dynamoDbEnhancedClientCustom.table(exportConfig.getExportTable(), EXPORT_TABLE_SCHEMA);
    }

    @Produces
    @DefaultBean
    public DynamoDbTable<DbUser> userTable(DynamoDbEnhancedClient dynamoDbEnhancedClientCustom) {
        return dynamoDbEnhancedClientCustom.table(exportConfig.getUserTable(), USER_TABLE_SCHEMA);
    }

    @Produces
    @DefaultBean
    public DynamoDbTable<DbCurrencyRate> currencyTable(DynamoDbEnhancedClient dynamoDbEnhancedClientCustom) {
        return dynamoDbEnhancedClientCustom.table(exportConfig.getCurrencyTable(), CURRENCY_TABLE_SCHEMA);
    }

    @Produces
    @Named("yyMmDdFormatter")
    public DateTimeFormatter yyMmDdFormatter() {
        return DateTimeFormatter.ofPattern(YYYY_MM_DD);
    }


    @Produces
    @Named("isoFormatter")
    public DateTimeFormatter isoFormatter() {
        return DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_SSS);
    }

}
