package com.github.storytime.lambda.common.model.db;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@DynamoDbBean
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DbUser {

    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String id;
    private String zenAuthToken;
    private String lastBackupDate;
    private String timeZone;

}