package com.github.storytime.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.github.storytime.lambda.api.ExportDbService;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.Instant;

import static com.github.storytime.lambda.api.configs.Constant.*;
import static com.github.storytime.lambda.common.utils.TimeUtils.timeBetween;
import static jakarta.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static java.time.Instant.now;
import static java.util.Map.of;
import static java.util.Optional.ofNullable;

public class FunctionExportHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    final ExportDbService exportDbService;
    final Logger logger;

    @Inject
    public FunctionExportHandler(final ExportDbService exportDbService,
                                 final Logger logger) {
        this.exportDbService = exportDbService;
        this.logger = logger;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, Context context) {

        final Instant lambdaStart = now();
        final var reqId = context.getAwsRequestId();

        try {
            logger.infof("====== Starting exporter, lambdaStart: [%d], reqId: [%s], msg: [%s]", lambdaStart.getEpochSecond(), reqId, input);

            final var pathParameters = input.getPathParameters();
            final var userId = ofNullable(pathParameters.get(DB_USER_ID_ATTRIBUTE))
                    .orElseThrow(() -> new RuntimeException("no user id found")).trim();
            final var type = Integer.parseInt(ofNullable(pathParameters.get(TYPE))
                    .orElseThrow(() -> new RuntimeException("no type id found")));

            final var export = exportDbService.findExport(userId);

            logger.infof("====== exporter api, type: [%s], userId: [%s], reqId: [%s]", type, userId, reqId);

            final var data = switch (type) {
                case OUT_YEAR_UAH -> export.get(OUT_YEAR_UAH);
                case OUT_QUARTER_UAH -> export.get(OUT_QUARTER_UAH);
                case OUT_MONTH_UAH -> export.get(OUT_MONTH_UAH);
                case IN_YEAR_UAH -> export.get(IN_YEAR_UAH);
                case IN_QUARTER_UAH -> export.get(IN_QUARTER_UAH);
                case IN_MONTH_UAH -> export.get(IN_MONTH_UAH);
                // USD
                case OUT_YEAR_USD -> export.get(OUT_YEAR_USD);
                case OUT_QUARTER_USD -> export.get(OUT_QUARTER_USD);
                case OUT_MONTH_USD -> export.get(OUT_MONTH_USD);
                case IN_YEAR_USD -> export.get(IN_YEAR_USD);
                case IN_QUARTER_USD -> export.get(IN_QUARTER_USD);
                case IN_MONTH_USD -> export.get(IN_MONTH_USD);
                // PROJECT
                case PROJECT_UAH_IN -> export.get(PROJECT_UAH_IN);
                case PROJECT_UAH_OUT -> export.get(PROJECT_UAH_OUT);
                case PROJECT_USD_IN -> export.get(PROJECT_USD_IN);
                case PROJECT_USD_OUT -> export.get(PROJECT_USD_OUT);

                default -> throw new IllegalStateException("No export type found");
            };

            final APIGatewayProxyResponseEvent response = buildResponse(data);
            logger.infof("====== Finished export, done for user: [%s], time: [%d], reqId: [%s]", userId, timeBetween(lambdaStart), reqId);
            return response;
        } catch (Exception e) {
            logger.errorf("Error api lambda, error: [%s], time: [%d], reqId: [%s]", e.getMessage(), timeBetween(lambdaStart), reqId, e);
            return buildResponse(EMPTY);
        }

    }

    private APIGatewayProxyResponseEvent buildResponse(final String data) {
        final APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setBody(data);
        response.setHeaders(of(CONTENT_TYPE, APPLICATION_JSON));
        response.setStatusCode(200);
        response.setIsBase64Encoded(false);
        return response;
    }
}


