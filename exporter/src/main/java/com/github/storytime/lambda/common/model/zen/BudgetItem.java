package com.github.storytime.lambda.common.model.zen;

public class BudgetItem {

    private String date;
    private int income;
    private boolean outcomeLock;
    private boolean incomeLock;
    private String tag;
    private int user;
    private int outcome;
    private int changed;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public boolean isOutcomeLock() {
        return outcomeLock;
    }

    public void setOutcomeLock(boolean outcomeLock) {
        this.outcomeLock = outcomeLock;
    }

    public boolean isIncomeLock() {
        return incomeLock;
    }

    public void setIncomeLock(boolean incomeLock) {
        this.incomeLock = incomeLock;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getOutcome() {
        return outcome;
    }

    public void setOutcome(int outcome) {
        this.outcome = outcome;
    }

    public int getChanged() {
        return changed;
    }

    public void setChanged(int changed) {
        this.changed = changed;
    }
}