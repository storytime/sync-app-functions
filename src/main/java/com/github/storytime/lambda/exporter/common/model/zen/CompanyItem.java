package com.github.storytime.lambda.exporter.common.model.zen;

public class CompanyItem {
    private int country;
    private Object fullTitle;
    private String www;
    private String countryCode;
    private int id;
    private String title;
    private int changed;

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public Object getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(Object fullTitle) {
        this.fullTitle = fullTitle;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChanged() {
        return changed;
    }

    public void setChanged(int changed) {
        this.changed = changed;
    }
}