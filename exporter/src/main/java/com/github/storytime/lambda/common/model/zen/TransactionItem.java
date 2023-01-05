package com.github.storytime.lambda.common.model.zen;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Accessors(chain = true)
public class TransactionItem implements Serializable {

    private String date;
    private Double income;
    private Double opIncome;
    private String originalPayee;
    private Double opOutcome;
    private Object latitude;
    private boolean hold;
    private String payee;
    private Object qrCode;
    private Integer opIncomeInstrument;
    private String id;
    private List<String> tag;
    private String outcomeBankID;
    private Double outcome;
    private Object opOutcomeInstrument;
    private Object longitude;
    private String outcomeAccount;
    private long created;
    private String incomeAccount;
    private String merchant;
    private Object reminderMarker;
    private boolean deleted;
    private String incomeBankID;
    private int outcomeInstrument;
    private String comment;
    private int user;
    private int incomeInstrument;
    private int changed;
    private boolean viewed;

    public boolean isAmountNotZero() {
        return (this.outcome != 0 && this.income == 0) || (this.outcome == 0 && this.income != 0);
    }
}