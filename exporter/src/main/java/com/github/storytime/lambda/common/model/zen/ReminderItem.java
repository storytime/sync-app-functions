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
public class ReminderItem implements Serializable {

    private int income;
    private String outcomeAccount;
    private Object endDate;
    private String incomeAccount;
    private Object merchant;
    private boolean notify;
    private List<Integer> points;
    private Object payee;
    private int outcomeInstrument;
    private int step;
    private Object interval;
    private Object comment;
    private String id;
    private List<String> tag;
    private int user;
    private int incomeInstrument;
    private int outcome;
    private String startDate;
    private int changed;
}