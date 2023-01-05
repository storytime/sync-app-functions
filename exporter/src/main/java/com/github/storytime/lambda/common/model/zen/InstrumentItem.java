package com.github.storytime.lambda.common.model.zen;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InstrumentItem implements Serializable {
    private String symbol;
    private double rate;
    private int id;
    private String shortTitle;
    private String title;
    private int changed;
}