package com.github.storytime.lambda.common.model.zen;

import java.io.Serializable;

public class TagItem implements Serializable {

    private String parent;
    private long color;
    private boolean budgetOutcome;
    private boolean showIncome;
    private String icon;
    private String title;
    private boolean required;
    private Object picture;
    private boolean budgetIncome;
    private String id;
    private boolean showOutcome;
    private int user;
    private int changed;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public long getColor() {
        return color;
    }

    public void setColor(long color) {
        this.color = color;
    }

    public boolean isBudgetOutcome() {
        return budgetOutcome;
    }

    public void setBudgetOutcome(boolean budgetOutcome) {
        this.budgetOutcome = budgetOutcome;
    }

    public boolean isShowIncome() {
        return showIncome;
    }

    public void setShowIncome(boolean showIncome) {
        this.showIncome = showIncome;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
        this.picture = picture;
    }

    public boolean isBudgetIncome() {
        return budgetIncome;
    }

    public void setBudgetIncome(boolean budgetIncome) {
        this.budgetIncome = budgetIncome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isShowOutcome() {
        return showOutcome;
    }

    public void setShowOutcome(boolean showOutcome) {
        this.showOutcome = showOutcome;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getChanged() {
        return changed;
    }

    public void setChanged(int changed) {
        this.changed = changed;
    }
}