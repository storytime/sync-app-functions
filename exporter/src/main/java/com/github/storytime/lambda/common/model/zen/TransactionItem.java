package com.github.storytime.lambda.common.model.zen;

import java.io.Serializable;
import java.util.List;


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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getOpIncome() {
        return opIncome;
    }

    public void setOpIncome(Double opIncome) {
        this.opIncome = opIncome;
    }

    public String getOriginalPayee() {
        return originalPayee;
    }

    public void setOriginalPayee(String originalPayee) {
        this.originalPayee = originalPayee;
    }

    public Double getOpOutcome() {
        return opOutcome;
    }

    public void setOpOutcome(Double opOutcome) {
        this.opOutcome = opOutcome;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public boolean isHold() {
        return hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public Object getQrCode() {
        return qrCode;
    }

    public void setQrCode(Object qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getOpIncomeInstrument() {
        return opIncomeInstrument;
    }

    public void setOpIncomeInstrument(Integer opIncomeInstrument) {
        this.opIncomeInstrument = opIncomeInstrument;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTag() {
        return tag;
    }

    public TransactionItem setTag(List<String> tag) {
        this.tag = tag;
        return this;
    }

    public String getOutcomeBankID() {
        return outcomeBankID;
    }

    public void setOutcomeBankID(String outcomeBankID) {
        this.outcomeBankID = outcomeBankID;
    }

    public Double getOutcome() {
        return outcome;
    }

    public void setOutcome(Double outcome) {
        this.outcome = outcome;
    }

    public Object getOpOutcomeInstrument() {
        return opOutcomeInstrument;
    }

    public void setOpOutcomeInstrument(Object opOutcomeInstrument) {
        this.opOutcomeInstrument = opOutcomeInstrument;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public String getOutcomeAccount() {
        return outcomeAccount;
    }

    public void setOutcomeAccount(String outcomeAccount) {
        this.outcomeAccount = outcomeAccount;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getIncomeAccount() {
        return incomeAccount;
    }

    public void setIncomeAccount(String incomeAccount) {
        this.incomeAccount = incomeAccount;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public Object getReminderMarker() {
        return reminderMarker;
    }

    public void setReminderMarker(Object reminderMarker) {
        this.reminderMarker = reminderMarker;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getIncomeBankID() {
        return incomeBankID;
    }

    public void setIncomeBankID(String incomeBankID) {
        this.incomeBankID = incomeBankID;
    }

    public int getOutcomeInstrument() {
        return outcomeInstrument;
    }

    public void setOutcomeInstrument(int outcomeInstrument) {
        this.outcomeInstrument = outcomeInstrument;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getIncomeInstrument() {
        return incomeInstrument;
    }

    public void setIncomeInstrument(int incomeInstrument) {
        this.incomeInstrument = incomeInstrument;
    }

    public int getChanged() {
        return changed;
    }

    public void setChanged(int changed) {
        this.changed = changed;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public boolean isAmountNotZero() {
        return (this.outcome != 0 && this.income == 0) || (this.outcome == 0 && this.income != 0);
    }
}