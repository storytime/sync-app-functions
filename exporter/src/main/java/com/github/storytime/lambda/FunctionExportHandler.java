package com.github.storytime.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.storytime.lambda.common.model.db.DbUser;
import com.github.storytime.lambda.common.model.req.RequestBody;
import com.github.storytime.lambda.common.model.zen.ZenResponse;
import com.github.storytime.lambda.common.service.UserService;
import com.github.storytime.lambda.common.service.ZenRestClientService;
import com.github.storytime.lambda.common.utils.TimeUtils;
import com.github.storytime.lambda.exporter.configs.ExportConfig;
import com.github.storytime.lambda.exporter.service.ExportDbService;
import com.github.storytime.lambda.exporter.service.ExportService;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.storytime.lambda.exporter.configs.Constant.*;
import static java.time.Instant.now;

public class FunctionExportHandler implements RequestHandler<SQSEvent, Integer> {
    @Inject
    @RestClient
    ZenRestClientService userRestClient;
    @Inject
    UserService userService;
    @Inject
    Logger logger;
    @Inject
    ExportService exportService;
    @Inject
    ExportDbService exportDbService;
    @Inject
    ExportConfig exportConfig;

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Integer handleRequest(final @NotNull SQSEvent message, Context context) {
        final Instant lambdaStart = now();
        final var reqId = context.getAwsRequestId();

        logger.infof("====== Starting exporter, lambdaStart: [%d], reqId: [%s], msg: [%s]", lambdaStart.getEpochSecond(), reqId, message);
        final String userId = message.getRecords().stream().findFirst().orElseThrow(() -> new RuntimeException("Cannot get SQS message")).getBody();

        final DbUser user = userService.findUserById(userId);
        final RequestBody body = new RequestBody(lambdaStart.getEpochSecond(), exportConfig.getStartFrom(), new HashSet<>());

        logger.infof("Fetching diff, for user: [%s], reqId: [%s]", userId, reqId);
        final String authToken = BEARER + SPACE + user.getZenAuthToken().trim();
        final ZenResponse zenData = userRestClient.getDiff(authToken, body);
        logger.infof("Fetched diff, for user: [%s], reqId: [%s]", userId, reqId);

        final var exportData = prepareExport(zenData, userId, reqId);

        exportDbService.saveExport(user, exportData);
        logger.infof("====== Finished export, done for user: [%s], time: [%d], reqId: [%s]", user.getId(), TimeUtils.timeBetween(lambdaStart), reqId);

        return HttpStatus.SC_OK;
    }

    private Map<Integer, String> prepareExport(final ZenResponse zenData,
                                               final String userId,
                                               final String reqId) {

        final Map<Integer, String> exportData = new LinkedHashMap<>();

        try {
            exportData.put(OUT_YEAR, objectMapper.writeValueAsString(exportService.getOutYearlyData(zenData)));
            exportData.put(OUT_QUARTER, objectMapper.writeValueAsString(exportService.getOutQuarterlyData(zenData)));
            exportData.put(OUT_MONTH, objectMapper.writeValueAsString(exportService.getOutMonthlyData(zenData)));
            exportData.put(IN_YEAR, objectMapper.writeValueAsString(exportService.getInYearlyData(zenData)));
            exportData.put(IN_QUARTER, objectMapper.writeValueAsString(exportService.getInQuarterData(zenData)));
            exportData.put(IN_MONTH, objectMapper.writeValueAsString(exportService.getInMonthlyData(zenData)));
        } catch (JsonProcessingException e) {
            logger.errorf("====== Cannot build export for user: [%s], req: [%s]", userId, reqId, e);
            throw new RuntimeException(e);
        }
        return exportData;
    }
}
