package com.github.storytime.lambda.common.model.db;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;

@DynamoDbBean
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DbCurrencyRate {

    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String id;

    private String currencySource;

    private String currencyType;

    private BigDecimal sellRate;

    private BigDecimal buyRate;
    private Long dateTime;

}