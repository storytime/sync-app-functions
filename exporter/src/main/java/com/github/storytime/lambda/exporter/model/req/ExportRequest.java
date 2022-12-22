package com.github.storytime.lambda.exporter.model.req;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@ApplicationScoped
public class ExportRequest {

    public UpdateItemRequest update(@NotBlank final String tableName,
                                    @NotEmpty final Map<String, AttributeValue> findBy,
                                    @NotEmpty final Map<String, AttributeValueUpdate> toUpdate) {
        return UpdateItemRequest.builder().tableName(tableName).key(findBy).attributeUpdates(toUpdate).build();
    }
}
