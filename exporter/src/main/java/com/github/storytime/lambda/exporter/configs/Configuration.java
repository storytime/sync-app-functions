package com.github.storytime.lambda.exporter.configs;

import com.github.storytime.lambda.common.model.db.DbCurrencyRate;
import com.github.storytime.lambda.common.model.db.DbExport;
import com.github.storytime.lambda.common.model.db.DbUser;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.format.DateTimeFormatter;

import static com.github.storytime.lambda.exporter.configs.Constant.DD_MM_YYYY_HH_MM_SS_SSS;
import static com.github.storytime.lambda.exporter.configs.Constant.YYYY_MM_DD;

@Dependent
public class Configuration {
    static final TableSchema<DbExport> EXPORT_TABLE_SCHEMA = TableSchema.fromClass(DbExport.class);
    static final TableSchema<DbUser> USER_TABLE_SCHEMA = TableSchema.fromClass(DbUser.class);
    static final TableSchema<DbCurrencyRate> CURRENCY_TABLE_SCHEMA = TableSchema.fromClass(DbCurrencyRate.class);

    private final ExportConfig exportConfig;

    @Inject
    public Configuration(final ExportConfig exportConfig) {
        this.exportConfig = exportConfig;
    }

    @Produces
    public DynamoDbEnhancedClient dynamoDbEnhancedClientCustom() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDbClient.builder()
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .httpClientBuilder(UrlConnectionHttpClient.builder())
                        .build())
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
