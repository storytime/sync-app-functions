package com.github.storytime.lambda.exporter.configs;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
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
