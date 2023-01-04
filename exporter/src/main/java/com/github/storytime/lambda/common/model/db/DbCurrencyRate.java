package com.github.storytime.lambda.common.model.db;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;

@DynamoDbBean
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DbCurrencyRate {


    private String id;

    private String currencySource;

    private String currencyType;

    private BigDecimal sellRate;

    private BigDecimal buyRate;
    private Long dateTime;

    @DynamoDbSortKey
    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}