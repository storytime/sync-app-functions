package com.github.storytime.lambda.backup.configs;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
