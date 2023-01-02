package com.github.storytime.lambda.backup.configs;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BackupConfig {


    @ConfigProperty(name = "table.user")
    String userTable;

    @ConfigProperty(name = "backup.zen.from")
    long startFrom;

    @ConfigProperty(name = "bucket.name")
    String bucket;

    @ConfigProperty(name = "backup.class")
    String storageClass;


    @ConfigProperty(name = "date.format")
    String dateFormat;

    public String getUserTable() {
        return userTable;
    }

    public void setUserTable(String userTable) {
        this.userTable = userTable;
    }

    public long getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(long startFrom) {
        this.startFrom = startFrom;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
