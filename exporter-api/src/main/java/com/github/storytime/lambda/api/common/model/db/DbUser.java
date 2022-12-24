package com.github.storytime.lambda.api.common.model.db;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class DbUser {

    String id;
    String zenAuthToken;
    String lastBackupDate;


    public DbUser(String id, String zenAuthToken, String lastBackupDate) {
        this.id = id;
        this.zenAuthToken = zenAuthToken;
        this.lastBackupDate = lastBackupDate;
    }

    public DbUser() {
    }

    @DynamoDbPartitionKey
    @DynamoDbSortKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZenAuthToken() {
        return zenAuthToken;
    }

    public void setZenAuthToken(String zenAuthToken) {
        this.zenAuthToken = zenAuthToken;
    }

    public String getLastBackupDate() {
        return lastBackupDate;
    }

    public void setLastBackupDate(String lastBackupDate) {
        this.lastBackupDate = lastBackupDate;
    }
}