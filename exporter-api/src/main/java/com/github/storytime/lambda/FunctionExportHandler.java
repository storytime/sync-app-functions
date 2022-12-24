package com.github.storytime.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.storytime.lambda.api.ExportDbService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.github.storytime.lambda.api.configs.Constant.*;
import static com.github.storytime.lambda.common.utils.TimeUtils.timeBetween;
import static java.time.Instant.now;
import static java.util.Map.of;
import static java.util.Optional.ofNullable;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.http.HttpStatus.SC_OK;

public class FunctionExportHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    ExportDbService exportDbService;
    @Inject
    Logger logger;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, Context context) {

        final Instant lambdaStart = now();
        final var reqId = context.getAwsRequestId();
        logger.infof("====== Starting exporter, lambdaStart: [%d], reqId: [%s], msg: [%s]", lambdaStart.getEpochSecond(), reqId, input);

        final var pathParameters = input.getPathParameters();
        final var userId = ofNullable(pathParameters.get(DB_USER_ID_ATTRIBUTE))
                .orElseThrow(() -> new RuntimeException("no user id found")).trim();
        final var type = Integer.parseInt(ofNullable(pathParameters.get(TYPE))
                .orElseThrow(() -> new RuntimeException("no type id found")));

        final var export = exportDbService.findExport(userId);

        final var data = switch (type) {
            case OUT_YEAR -> export.get(OUT_YEAR);
            case OUT_QUARTER -> export.get(OUT_QUARTER);
            case OUT_MONTH -> export.get(OUT_MONTH);
            case IN_YEAR -> export.get(IN_YEAR);
            case IN_QUARTER -> export.get(IN_QUARTER);
            case IN_MONTH -> export.get(IN_MONTH);
            default -> throw new IllegalStateException("No export type found");
        };

        APIGatewayProxyResponseEvent response = buildResponse(data, userId, reqId);
        logger.infof("====== Finished export, done for user: [%s], time: [%d], reqId: [%s]", userId, timeBetween(lambdaStart), reqId);
        return response;
    }

    private APIGatewayProxyResponseEvent buildResponse(final List<Map<String, String>> data,
                                                       final String userId,
                                                       final String reqId) {
        final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        try {
            response.setBody(objectMapper.writeValueAsString(data));
            response.setHeaders(of(CONTENT_TYPE, APPLICATION_JSON));
            response.setStatusCode(SC_OK);
            response.setIsBase64Encoded(false);
            return response;
        } catch (Exception e) {
            logger.errorf("====== Cannot build export for user: [%s], reqId: [%s]", userId, reqId, e);
            return response;
        }
    }
}


