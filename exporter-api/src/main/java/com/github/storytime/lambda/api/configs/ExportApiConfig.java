package com.github.storytime.lambda.api.configs;

import lombok.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportApiConfig {

    @ConfigProperty(name = "table.export")
    String exportTable;


    public String getExportTable() {
        return exportTable;
    }

    public void setExportTable(String exportTable) {
        this.exportTable = exportTable;
    }

}
