package com.github.storytime.lambda.stater;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StarterConfig {

    @ConfigProperty(name = "table.user")
    String userTable;

    @ConfigProperty(name = "queue.url")
    String queueUrl;

    public String getUserTable() {
        return userTable;
    }

    public void setUserTable(String userTable) {
        this.userTable = userTable;
    }

    public String getQueueUrl() {
        return queueUrl;
    }

    public void setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
    }
}
