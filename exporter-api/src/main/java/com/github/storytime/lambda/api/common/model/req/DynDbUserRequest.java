package com.github.storytime.lambda.api.common.model.req;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@ApplicationScoped
public class DynDbUserRequest {
    public GetItemRequest getRequest(@NotBlank final String tableName,
                                     @NotEmpty final Map<String, AttributeValue> dbAttribute) {
        return GetItemRequest.builder().tableName(tableName).key(dbAttribute).build();
    }
}
