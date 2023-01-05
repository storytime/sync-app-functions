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
public class ReminderMarkerItem implements Serializable {

    private String date;
    private int income;
    private String outcomeAccount;
    private String reminder;
    private String incomeAccount;
    private Object merchant;
    private boolean notify;
    private Object payee;
    private int outcomeInstrument;
    private Object comment;
    private String id;
    private String state;
    private List<String> tag;
    private int user;
    private int incomeInstrument;
    private int outcome;
    private int changed;
}