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
    private Object role;

    private Object payoffInterval;

    private int instrument;

    private String type;

    private String title;

    private Object percent;

    private boolean enableSMS;

    private double balance;

    private Object payoffStep;

    private double creditLimit;

    private Integer company;

    private Object endDateOffset;

    private String id;

    private Boolean savings;

    private double startBalance;

    private boolean inBalance;

    private boolean enableCorrection;

    private boolean archive;

    private List<String> syncID;

    private Object capitalization;

    private Object endDateOffsetInterval;

    private int user;

    private Object startDate;

    private long changed;

}