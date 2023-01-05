package com.github.storytime.lambda.common.model.zen;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CountryItem implements Serializable {
    private String domain;
    private int currency;
    private int id;
    private String title;
}