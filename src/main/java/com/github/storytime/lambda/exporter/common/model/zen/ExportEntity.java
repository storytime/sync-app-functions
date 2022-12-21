package com.github.storytime.lambda.exporter.common.model.zen;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@RegisterForReflection
public class ExportEntity {

    private String userId;

    public ExportEntity(String userId) {
        this.userId = userId;
    }

    public ExportEntity() {
    }

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}