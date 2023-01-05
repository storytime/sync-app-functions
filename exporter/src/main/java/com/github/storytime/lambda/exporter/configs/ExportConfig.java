package com.github.storytime.lambda.exporter.configs;

import lombok.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExportConfig {


    @ConfigProperty(name = "table.user")
    String userTable;

    @ConfigProperty(name = "table.export")
    String exportTable;

    @ConfigProperty(name = "table.currency")
    String currencyTable;

    @ConfigProperty(name = "export.zen.from")
    long startFrom;

}
