package com.github.storytime.lambda.exporter.common.mapper;

import com.github.storytime.lambda.exporter.common.model.db.User;
import com.github.storytime.lambda.exporter.configs.Constant;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@ApplicationScoped
public class UserMapper {

    @NotNull
    public User userOf(@NotEmpty final Map<String, AttributeValue> item) {
        return new User(
                item.get(Constant.DB_ID_ATTRIBUTE).s(),
                item.get(Constant.DB_ATTRIBUTE_ZEN_TOKEN).s(),
                null
        );
    }
}
