package com.github.storytime.lambda.common.model.pb;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CashResponse {
    private String sale;
    private String baseCcy;
    private String buy;
    private String ccy;
}