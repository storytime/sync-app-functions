package com.github.storytime.lambda.exporter.common.model.db;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.List;
import java.util.Map;

@DynamoDbBean
public class DbExport {
    private String userId;

    private Map<String, List<Map<String, String>>> data;

    public DbExport(final String userId, final Map<String, List<Map<String, String>>> data) {
        this.userId = userId;
        this.data = data;
    }

    public DbExport() {
    }

    @DynamoDbPartitionKey
    @DynamoDbSortKey
    public String getUserId() { return this.userId; }
    public void setUserId(String userId) { this.userId = userId; }


    public Map<String, List<Map<String, String>>> getData() {
        return data;
    }

    public void setData(Map<String, List<Map<String, String>>> data) {
        this.data = data;
    }
}