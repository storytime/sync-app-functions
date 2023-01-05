package com.github.storytime.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.storytime.lambda.common.mapper.ZenCommonMapper;
import com.github.storytime.lambda.common.model.db.DbUser;
import com.github.storytime.lambda.common.model.req.RequestBody;
import com.github.storytime.lambda.common.model.zen.ZenResponse;
import com.github.storytime.lambda.common.service.DbUserService;
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
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.concat;

public class FunctionExportHandler implements RequestHandler<SQSEvent, Integer> {

    @Inject
    @RestClient
    ZenRestClientService userRestClient;
    @Inject
    DbUserService userService;
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

    @Inject
    ZenCommonMapper zenCommonMapper;

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
        final ZenResponse zenDataInRaw = userRestClient.getDiff(authToken, body);
        logger.infof("Fetched diff, for user: [%s], reqId: [%s]", userId, reqId);

        final var zenDataInUAH = zenCommonMapper.correctCreateDate(zenDataInRaw);
        final var inUah = writeAsJsonString(zenDataInUAH, OUT_YEAR_UAH, OUT_QUARTER_UAH, OUT_MONTH_UAH, IN_YEAR_UAH, IN_QUARTER_UAH, IN_MONTH_UAH);
        final var zenDataInUSD = zenCommonMapper.mapToUSD(zenDataInUAH.toBuilder().build());
        final var inUsd = writeAsJsonString(zenDataInUSD, OUT_YEAR_USD, OUT_QUARTER_USD, OUT_MONTH_USD, IN_YEAR_USD, IN_QUARTER_USD, IN_MONTH_USD);

        final var exportData = concat(inUah.entrySet().stream(), inUsd.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        exportDbService.saveExport(user, exportData);
        logger.infof("====== Finished export, done for user: [%s], time: [%d], reqId: [%s]", user.getId(), TimeUtils.timeBetween(lambdaStart), reqId);

        return HttpStatus.SC_OK;
    }

    private Map<Integer, String> writeAsJsonString(final ZenResponse zenData,
                                                   final Integer outYearUah,
                                                   final Integer outQuarterUah,
                                                   final Integer outMonthUah,
                                                   final Integer inYearUah,
                                                   final Integer inQuarterUah,
                                                   final Integer inMonthUah) {
        try {
            final Map<Integer, String> exportData = new LinkedHashMap<>();
            exportData.put(outYearUah, objectMapper.writeValueAsString(exportService.getOutYearlyData(zenData)));
            exportData.put(outQuarterUah, objectMapper.writeValueAsString(exportService.getOutQuarterlyData(zenData)));
            exportData.put(outMonthUah, objectMapper.writeValueAsString(exportService.getOutMonthlyData(zenData)));
            exportData.put(inYearUah, objectMapper.writeValueAsString(exportService.getInYearlyData(zenData)));
            exportData.put(inQuarterUah, objectMapper.writeValueAsString(exportService.getInQuarterData(zenData)));
            exportData.put(inMonthUah, objectMapper.writeValueAsString(exportService.getInMonthlyData(zenData)));
            return exportData;
        } catch (JsonProcessingException e) {
            logger.errorf("====== Cannot build export fo", e);
            throw new RuntimeException(e);
        }
    }
}
