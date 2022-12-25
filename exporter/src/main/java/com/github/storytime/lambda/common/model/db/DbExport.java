package com.github.storytime.lambda.common.model.db;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.Map;

@DynamoDbBean
public class DbExport {
    private String userId;

    private Map<Integer, String> data;

    public DbExport(final String userId, final Map<Integer, String> data) {
        this.userId = userId;
        this.data = data;
    }

    public DbExport() {
    }

    @DynamoDbPartitionKey
    @DynamoDbSortKey
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Map<Integer, String> getData() {
        return data;
    }

    public void setData(Map<Integer, String> data) {
        this.data = data;
    }
}