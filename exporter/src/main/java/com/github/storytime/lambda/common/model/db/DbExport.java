package com.github.storytime.lambda.common.model.db;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Map;

@DynamoDbBean
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DbExport {

    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String userId;
    private Map<Integer, String> data;
}