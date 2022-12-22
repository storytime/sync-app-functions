package com.github.storytime.lambda.exporter.common.model.zen;

import java.util.List;

public class AccountItem {

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

    public boolean isJsonMemberPrivate() {
        return jsonMemberPrivate;
    }

    public void setJsonMemberPrivate(boolean jsonMemberPrivate) {
        this.jsonMemberPrivate = jsonMemberPrivate;
    }

    public Object getRole() {
        return role;
    }

    public void setRole(Object role) {
        this.role = role;
    }

    public Object getPayoffInterval() {
        return payoffInterval;
    }

    public void setPayoffInterval(Object payoffInterval) {
        this.payoffInterval = payoffInterval;
    }

    public int getInstrument() {
        return instrument;
    }

    public void setInstrument(int instrument) {
        this.instrument = instrument;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getPercent() {
        return percent;
    }

    public void setPercent(Object percent) {
        this.percent = percent;
    }

    public boolean isEnableSMS() {
        return enableSMS;
    }

    public void setEnableSMS(boolean enableSMS) {
        this.enableSMS = enableSMS;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Object getPayoffStep() {
        return payoffStep;
    }

    public void setPayoffStep(Object payoffStep) {
        this.payoffStep = payoffStep;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    public Object getEndDateOffset() {
        return endDateOffset;
    }

    public void setEndDateOffset(Object endDateOffset) {
        this.endDateOffset = endDateOffset;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSavings() {
        return savings;
    }

    public void setSavings(Boolean savings) {
        this.savings = savings;
    }

    public double getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(double startBalance) {
        this.startBalance = startBalance;
    }

    public boolean isInBalance() {
        return inBalance;
    }

    public void setInBalance(boolean inBalance) {
        this.inBalance = inBalance;
    }

    public boolean isEnableCorrection() {
        return enableCorrection;
    }

    public void setEnableCorrection(boolean enableCorrection) {
        this.enableCorrection = enableCorrection;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public List<String> getSyncID() {
        return syncID;
    }

    public void setSyncID(List<String> syncID) {
        this.syncID = syncID;
    }

    public Object getCapitalization() {
        return capitalization;
    }

    public void setCapitalization(Object capitalization) {
        this.capitalization = capitalization;
    }

    public Object getEndDateOffsetInterval() {
        return endDateOffsetInterval;
    }

    public void setEndDateOffsetInterval(Object endDateOffsetInterval) {
        this.endDateOffsetInterval = endDateOffsetInterval;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Object getStartDate() {
        return startDate;
    }

    public void setStartDate(Object startDate) {
        this.startDate = startDate;
    }

    public long getChanged() {
        return changed;
    }

    public void setChanged(long changed) {
        this.changed = changed;
    }
}