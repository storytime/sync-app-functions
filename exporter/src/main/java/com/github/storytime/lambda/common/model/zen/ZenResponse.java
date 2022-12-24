package com.github.storytime.lambda.common.model.zen;

import java.util.List;

public class ZenResponse {
    private long serverTimestamp;
    private List<CountryItem> country;
    private List<ReminderMarkerItem> reminderMarker;
    private List<ReminderItem> reminder;
    private List<MerchantItem> merchant;
    private List<InstrumentItem> instrument;
    private List<CompanyItem> company;
    private List<TagItem> tag;
    private List<UserItem> user;
    private List<AccountItem> account;
    private List<TransactionItem> transaction;
    private List<BudgetItem> budget;

    public long getServerTimestamp() {
        return serverTimestamp;
    }

    public void setServerTimestamp(long serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }

    public List<CountryItem> getCountry() {
        return country;
    }

    public void setCountry(List<CountryItem> country) {
        this.country = country;
    }

    public List<ReminderMarkerItem> getReminderMarker() {
        return reminderMarker;
    }

    public void setReminderMarker(List<ReminderMarkerItem> reminderMarker) {
        this.reminderMarker = reminderMarker;
    }

    public List<ReminderItem> getReminder() {
        return reminder;
    }

    public void setReminder(List<ReminderItem> reminder) {
        this.reminder = reminder;
    }

    public List<MerchantItem> getMerchant() {
        return merchant;
    }

    public void setMerchant(List<MerchantItem> merchant) {
        this.merchant = merchant;
    }

    public List<InstrumentItem> getInstrument() {
        return instrument;
    }

    public void setInstrument(List<InstrumentItem> instrument) {
        this.instrument = instrument;
    }

    public List<CompanyItem> getCompany() {
        return company;
    }

    public void setCompany(List<CompanyItem> company) {
        this.company = company;
    }

    public List<TagItem> getTag() {
        return tag;
    }

    public void setTag(List<TagItem> tag) {
        this.tag = tag;
    }

    public List<UserItem> getUser() {
        return user;
    }

    public void setUser(List<UserItem> user) {
        this.user = user;
    }

    public List<AccountItem> getAccount() {
        return account;
    }

    public void setAccount(List<AccountItem> account) {
        this.account = account;
    }

    public List<TransactionItem> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<TransactionItem> transaction) {
        this.transaction = transaction;
    }

    public List<BudgetItem> getBudget() {
        return budget;
    }

    public void setBudget(List<BudgetItem> budget) {
        this.budget = budget;
    }
}