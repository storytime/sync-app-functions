package com.github.storytime.lambda.common.model.pb;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ArchiveRateResponse {

    private String baseCurrency;
    private String currency;
    private BigDecimal saleRateNB;
    private BigDecimal purchaseRateNB;
    private BigDecimal saleRate;
    private BigDecimal purchaseRate;
}