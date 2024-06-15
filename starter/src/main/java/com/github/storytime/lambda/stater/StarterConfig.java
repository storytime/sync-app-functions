package com.github.storytime.lambda.stater;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StarterConfig {

    @ConfigProperty(name = "table.user")
    String userTable;

    @ConfigProperty(name = "queue.url")
    String queueUrl;
}
