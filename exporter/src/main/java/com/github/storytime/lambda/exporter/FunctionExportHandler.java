package com.github.storytime.lambda.exporter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.github.storytime.lambda.exporter.common.model.db.DbUser;
import com.github.storytime.lambda.exporter.common.model.req.RequestBody;
import com.github.storytime.lambda.exporter.common.model.zen.ZenResponse;
import com.github.storytime.lambda.exporter.common.service.UserService;
import com.github.storytime.lambda.exporter.common.service.ZenRestClientService;
import com.github.storytime.lambda.exporter.common.utils.TimeUtils;
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
import java.util.List;
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

    @Override
    public Integer handleRequest(final @NotNull SQSEvent message, Context context) {
        final Instant lambdaStart = now();
        final var reqId = context.getAwsRequestId();

        logger.infof("====== Starting exporter, lambdaStart: [%d], reqId: [%s], msg: [%s]", lambdaStart.getEpochSecond(), reqId, message);
        final String userId = message.getRecords()
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot get SQS message"))
                .getBody();

        final DbUser user = userService.findUserById(userId);
        final RequestBody body = new RequestBody(lambdaStart.getEpochSecond(), exportConfig.getStartFrom(), new HashSet<>());
        final ZenResponse zenData = userRestClient.getDiff("Bearer" + SPACE + user.getZenAuthToken().trim(), body);

        final Map<String, List<Map<String, String>>> exportData = new LinkedHashMap<>();
        exportData.put(OUT_YEAR, exportService.getOutYearlyData(zenData));
        exportData.put(OUT_MONTH, exportService.getOutMonthlyData(zenData));
        exportData.put(OUT_QUARTER, exportService.getOutQuarterlyData(zenData));
        exportData.put(IN_MONTH, exportService.getInMonthlyData(zenData));
        exportData.put(IN_QUARTER, exportService.getInQuarterData(zenData));
        exportData.put(IN_YEAR, exportService.getInYearlyData(zenData));

        exportDbService.saveExport(user, exportData);
        logger.infof("====== Finished export, done for user: [%s], time: [%d], reqId: [%s]", user.getId(), TimeUtils.timeBetween(lambdaStart), reqId);

        return HttpStatus.SC_OK;
    }
}
