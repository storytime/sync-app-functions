package com.github.storytime.lambda.common.model.db;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.Map;

@DynamoDbBean
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DbExport {

    private String userId;
    private Map<Integer, String> data;

    @DynamoDbPartitionKey
    @DynamoDbSortKey
    public String getUserId() {
        return userId;
    }
}