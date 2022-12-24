package com.github.storytime.lambda.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.storytime.lambda.api.common.utils.TimeUtils;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.storytime.lambda.api.configs.Constant.*;
import static java.time.Instant.now;

public class FunctionExportHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    ExportDbService exportDbService;
    @Inject
    Logger logger;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, Context context) {

        logger.infof("====== Starting " + input);

        final Instant lambdaStart = now();
        final var reqId = context.getAwsRequestId();
        final var pathParameters = input.getPathParameters();

        final var userId = Optional.ofNullable(pathParameters.get("userId"))
                .orElseThrow(() -> new RuntimeException("no user id found")).trim();
        final var type = Optional.ofNullable(pathParameters.get("type"))
                .orElseThrow(() -> new RuntimeException("no user id found")).trim();

        logger.infof("====== Starting exporter, lambdaStart: [%d], reqId: [%s], msg: [%s]", lambdaStart.getEpochSecond(), reqId, input);
        final var export = exportDbService.findExport(userId);

        logger.infof("====== Finished export, done for user: [%s], time: [%d], reqId: [%s]", userId, TimeUtils.timeBetween(lambdaStart), reqId);

        final var data = switch (type) {
            case OUT_YEAR -> export.get(OUT_YEAR);
            case OUT_QUARTER -> export.get(OUT_QUARTER);
            case OUT_MONTH -> export.get(OUT_MONTH);
            case IN_YEAR -> export.get(IN_YEAR);
            case IN_QUARTER -> export.get(IN_QUARTER);
            case IN_MONTH -> export.get(IN_MONTH);
            default -> throw new IllegalStateException("Invalid day: ");
        };

        APIGatewayProxyResponseEvent p = new APIGatewayProxyResponseEvent();
        p.setHeaders(Map.of("Content-Type", "application/json"));
        try {
            p.setBody(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        p.setStatusCode(200);
        p.setIsBase64Encoded(false);


        return p;
    }
}
