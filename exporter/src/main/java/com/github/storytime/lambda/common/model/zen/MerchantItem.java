package com.github.storytime.lambda.common.model.zen;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MerchantItem implements Serializable {

    private String id;
    private String title;
    private Object user;
    private int changed;
}