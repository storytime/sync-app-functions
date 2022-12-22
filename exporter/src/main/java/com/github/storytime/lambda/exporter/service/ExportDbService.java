package com.github.storytime.lambda.exporter.service;


import com.github.storytime.lambda.exporter.common.model.db.User;
import com.github.storytime.lambda.exporter.common.utils.TimeUtils;
import com.github.storytime.lambda.exporter.configs.Constant;
import com.github.storytime.lambda.exporter.configs.ExportConfig;
import com.github.storytime.lambda.exporter.model.req.ExportRequest;
import io.smallrye.common.constraint.NotNull;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

import static java.time.Instant.now;
import static java.util.stream.Collectors.toMap;
import static software.amazon.awssdk.services.dynamodb.model.AttributeAction.PUT;
import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder;

@ApplicationScoped
public class ExportDbService {

    @Inject
    Logger logger;

    @Inject
    ExportRequest exportRequest;

    @Inject
    ExportConfig exportConfig;

    @Inject
    DynamoDbClient dynamoDBSync;

    public void saveExport(final @NotNull User user,
                           final @NotEmpty Map<String, String> exportData) {
        final var start = now();
        final var id = user.id();

        logger.infof("Saving export for user: [%s] - started ...", id);
        final var findBy = Map.of(Constant.DB_USER_ID_ATTRIBUTE, builder().s(id).build());

        final Map<String, AttributeValueUpdate> toUpdate = exportData.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> createAttribute(e.getValue())));

        final UpdateItemRequest updateRequest = exportRequest.update(exportConfig.getExportTable(), findBy, toUpdate);
        final UpdateItemResponse updateResponse = dynamoDBSync.updateItem(updateRequest);

        logger.infof("Saved export, user: [%s], isSuccessful: [%b], time: [%d]", id, updateResponse.sdkHttpResponse().isSuccessful(), TimeUtils.timeBetween(start));
    }

    private AttributeValueUpdate createAttribute(final String data) {
        return AttributeValueUpdate.builder()
                .value(v -> v.s(data))
                .action(PUT)
                .build();
    }
}
