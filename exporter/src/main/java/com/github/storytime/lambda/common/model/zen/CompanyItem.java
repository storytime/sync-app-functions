package com.github.storytime.lambda.common.model.zen;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CompanyItem implements Serializable {
    private int country;
    private Object fullTitle;
    private String www;
    private String countryCode;
    private int id;
    private String title;
    private int changed;
}