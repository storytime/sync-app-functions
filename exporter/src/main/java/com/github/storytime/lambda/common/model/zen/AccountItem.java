package com.github.storytime.lambda.common.model.zen;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountItem implements Serializable {

    private boolean jsonMemberPrivate;

    private int instrument;

    private String type;

    private String title;

    private boolean enableSMS;

    private double balance;

    private double creditLimit;

    private Integer company;

    private String id;

    private Boolean savings;

    private double startBalance;

    private boolean inBalance;

    private boolean enableCorrection;

    private boolean archive;

    private List<String> syncID;

    private int user;

    private long changed;

}