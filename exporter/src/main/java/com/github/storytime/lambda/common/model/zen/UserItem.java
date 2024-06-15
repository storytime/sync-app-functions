package com.github.storytime.lambda.common.model.zen;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserItem implements Serializable {

    private int country;
    private long paidTill;
    private String countryCode;
    private int currency;
    private int id;
    private String subscription;
    private String login;
    private int changed;
}