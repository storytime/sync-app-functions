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
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.storytime.lambda.common.utils.TimeUtils.timeBetween;
import static com.github.storytime.lambda.exporter.configs.Constant.*;
import static java.time.Instant.now;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.concat;

public class FunctionExportHandler implements RequestHandler<SQSEvent, Integer> {

    private final ZenRestClientService userRestClient;
    private final DbUserService userService;
    private final Logger logger;
    private final ExportService exportService;
    private final ExportDbService exportDbService;
    private final ExportConfig exportConfig;
    private final ObjectMapper jsonMapper;
    private final ZenCommonMapper zenCommonMapper;


    @Inject
    public FunctionExportHandler(@RestClient final ZenRestClientService userRestClient,
                                 final DbUserService userService,
                                 final Logger logger,
                                 final ExportService exportService,
                                 final ExportDbService exportDbService,
                                 final ExportConfig exportConfig,
                                 final ObjectMapper jsonMapper,
                                 final ZenCommonMapper zenCommonMapper) {
        this.userRestClient = userRestClient;
        this.userService = userService;
        this.logger = logger;
        this.exportService = exportService;
        this.exportDbService = exportDbService;
        this.exportConfig = exportConfig;
        this.jsonMapper = jsonMapper;
        this.zenCommonMapper = zenCommonMapper;
    }

    @Override
    public Integer handleRequest(final SQSEvent message, Context context) {

        final Instant lambdaStart = now();
        final var reqId = context.getAwsRequestId();

        try {
            logger.infof("=== Starting exporter, lambdaStart: [%d], reqId: [%s], msg: [%s]", lambdaStart.getEpochSecond(), reqId, message);
            final String userId = message.getRecords().stream().findFirst().orElseThrow(() -> new RuntimeException("Cannot get SQS message")).getBody();
            final DbUser user = userService.findUserById(userId);

            final RequestBody body = new RequestBody(lambdaStart.getEpochSecond(), exportConfig.getStartFrom(), new HashSet<>());
            logger.infof("Fetching diff, for user: [%s], reqId: [%s]", userId, reqId);
            final String authToken = BEARER + SPACE + user.getZenAuthToken().trim();
            final ZenResponse zenDataInRaw = userRestClient.getDiff(authToken, body);
            logger.infof("Fetched diff, for user: [%s], reqId: [%s]", userId, reqId);

            ZenResponse inUahClone = zenCommonMapper.mapToUAH(ZenCommonMapper.copyObject(zenDataInRaw), user);
            final var inUah = writeAsJsonString(inUahClone, OUT_YEAR_UAH, OUT_QUARTER_UAH, OUT_MONTH_UAH, IN_YEAR_UAH, IN_QUARTER_UAH, IN_MONTH_UAH, PROJECT_UAH_IN, PROJECT_UAH_OUT);

            ZenResponse zenDataInUSD = zenCommonMapper.mapToUSD(ZenCommonMapper.copyObject(zenDataInRaw), user);
            final var inUsd = writeAsJsonString(zenDataInUSD, OUT_YEAR_USD, OUT_QUARTER_USD, OUT_MONTH_USD, IN_YEAR_USD, IN_QUARTER_USD, IN_MONTH_USD, PROJECT_USD_IN, PROJECT_USD_OUT);

            final var exportData = concat(inUah.entrySet().stream(), inUsd.entrySet().stream())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

            exportDbService.saveExport(user, exportData);
            logger.infof("====== Finished export, done for user: [%s], time: [%d], reqId: [%s]", user.getId(), TimeUtils.timeBetween(lambdaStart), reqId);

            return 200;
        } catch (Exception ex) {
            logger.errorf("Error in lambda, error: [%s], time: [%d], reqId: [%s]", ex.getMessage(), timeBetween(lambdaStart), reqId, ex);
            return 500;
        }

    }


    private Map<Integer, String> writeAsJsonString(final ZenResponse zenData,
                                                   final Integer outYearUah,
                                                   final Integer outQuarterUah,
                                                   final Integer outMonthUah,
                                                   final Integer inYearUah,
                                                   final Integer inQuarterUah,
                                                   final Integer inMonthUah,
                                                   final Integer projectIn,
                                                   final Integer projectOut) {
        try {
            final Map<Integer, String> exportData = new LinkedHashMap<>();
            exportData.put(outYearUah, jsonMapper.writeValueAsString(exportService.getOutYearlyDataByCategory(zenData)));
            exportData.put(outQuarterUah, jsonMapper.writeValueAsString(exportService.getOutQuarterlyDataByCategory(zenData)));
            exportData.put(outMonthUah, jsonMapper.writeValueAsString(exportService.getOutMonthlyDataByCategory(zenData)));
            exportData.put(inYearUah, jsonMapper.writeValueAsString(exportService.getInYearlyDataByCategory(zenData)));
            exportData.put(inQuarterUah, jsonMapper.writeValueAsString(exportService.getInQuarterDataByCategory(zenData)));
            exportData.put(inMonthUah, jsonMapper.writeValueAsString(exportService.getInMonthlyDataByCategory(zenData)));

            // Project
            exportData.put(projectIn, jsonMapper.writeValueAsString(exportService.getInDataByProject(zenData)));
            exportData.put(projectOut, jsonMapper.writeValueAsString(exportService.getOutDataByProject(zenData)));

            return exportData;
        } catch (JsonProcessingException e) {
            logger.errorf("====== Cannot build export fo", e);
            throw new RuntimeException(e);
        }
    }
}
