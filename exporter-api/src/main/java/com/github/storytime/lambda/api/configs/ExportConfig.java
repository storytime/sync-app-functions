package com.github.storytime.lambda.api.configs;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExportConfig {


    @ConfigProperty(name = "table.user")
    String userTable;

    @ConfigProperty(name = "table.export")
    String exportTable;

    @ConfigProperty(name = "export.zen.from")
    long startFrom;

    public long getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(long startFrom) {
        this.startFrom = startFrom;
    }

    public String getExportTable() {
        return exportTable;
    }

    public void setExportTable(String exportTable) {
        this.exportTable = exportTable;
    }

    public String getUserTable() {
        return userTable;
    }

    public void setUserTable(String userTable) {
        this.userTable = userTable;
    }


}
