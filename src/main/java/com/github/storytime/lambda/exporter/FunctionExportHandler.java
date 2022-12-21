package com.github.storytime.lambda.exporter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.storytime.lambda.exporter.configs.ExportConfig;
import com.github.storytime.lambda.exporter.common.model.zen.TagItem;
import com.github.storytime.lambda.exporter.common.utils.TimeUtils;
import com.github.storytime.lambda.exporter.common.model.db.User;
import com.github.storytime.lambda.exporter.common.model.req.RequestBody;
import com.github.storytime.lambda.exporter.common.model.zen.ZenResponse;
import com.github.storytime.lambda.exporter.service.ExportDbService;
import com.github.storytime.lambda.exporter.service.ExportService;
import com.github.storytime.lambda.exporter.common.service.ZenRestClientService;
import com.github.storytime.lambda.exporter.common.service.UserService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static java.time.Instant.now;
import static com.github.storytime.lambda.exporter.configs.Constant.*;

public class FunctionExportHandler implements RequestHandler<SQSEvent, List<TagItem>> {
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
    ObjectMapper mapper;
    @Inject
    ExportConfig exportConfig;

    @Override
    public List<TagItem> handleRequest(final @NotNull SQSEvent message, Context context) {
        final Instant lambdaStart = now();
        final var reqId = context.getAwsRequestId();
        try {

            logger.infof("====== Starting exporter, lambdaStart: [%d], reqId: [%s], msg: [%s]", lambdaStart.getEpochSecond(), reqId, message);
            final String userId = message.getRecords()
                    .stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Cannot get SQS message"))
                    .getBody();

            final User user = userService.findUserById(userId);
            final RequestBody body = new RequestBody(lambdaStart.getEpochSecond(), exportConfig.getStartFrom(), new HashSet<>());
            final ZenResponse zenData = userRestClient.getDiff("Bearer" + SPACE + user.zenAuthToken().trim(), body);

            final Map<String, String> exportData = new HashMap<>();
            exportData.put(OUT_YEAR, mapper.writeValueAsString(exportService.getOutYearlyData(zenData)));
            exportData.put(OUT_MONTH, mapper.writeValueAsString(exportService.getOutMonthlyData(zenData)));
            exportData.put(OUT_QUARTER, mapper.writeValueAsString(exportService.getOutQuarterlyData(zenData)));
            exportData.put(IN_MONTH, mapper.writeValueAsString(exportService.getInMonthlyData(zenData)));
            exportData.put(IN_QUARTER, mapper.writeValueAsString(exportService.getInQuarterData(zenData)));
            exportData.put(IN_YEAR, mapper.writeValueAsString(exportService.getInYearlyData(zenData)));

            exportDbService.saveExport(user, exportData);
            logger.infof("====== Finished export, done for user: [%s], time: [%d], reqId: [%s]", user.id(), TimeUtils.timeBetween(lambdaStart), reqId);
            return zenData.getTag();
        } catch (JsonProcessingException e) {
            logger.errorf("====== Error export, done for user: [%s], time: [%d], reqId: [%s]", null, TimeUtils.timeBetween(lambdaStart), reqId, e);
            throw new RuntimeException(e);
        }
    }
}
