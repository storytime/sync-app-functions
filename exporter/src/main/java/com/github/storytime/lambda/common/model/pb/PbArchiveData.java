package com.github.storytime.lambda.common.model.pb;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PbArchiveData {

    private String date;
    private String bank;
    private Integer baseCurrency;
    private String baseCurrencyLit;
    private List<ArchiveRateResponse> exchangeRate;

}
