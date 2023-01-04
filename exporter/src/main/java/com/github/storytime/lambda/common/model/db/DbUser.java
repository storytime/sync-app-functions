package com.github.storytime.lambda.common.model.db;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DbUser {

    private String id;
    private String zenAuthToken;
    private String lastBackupDate;

    @DynamoDbPartitionKey
    @DynamoDbSortKey
    public String getId() {
        return id;
    }

}