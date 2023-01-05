package com.github.storytime.lambda.common.model.zen;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BudgetItem implements Serializable {

    private String date;
    private int income;
    private boolean outcomeLock;
    private boolean incomeLock;
    private String tag;
    private int user;
    private int outcome;
    private int changed;
}