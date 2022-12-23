package com.github.storytime.lambda.api;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@ApplicationScoped
public class ExportMapper {

    @NotNull
    public Map<String, String> mapExportData(@NotEmpty final Map<String, AttributeValue> item) {
        return item.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().s()));

    }
}
