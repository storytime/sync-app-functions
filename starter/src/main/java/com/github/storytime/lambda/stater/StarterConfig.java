package com.github.storytime.lambda.stater;

import lombok.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

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
