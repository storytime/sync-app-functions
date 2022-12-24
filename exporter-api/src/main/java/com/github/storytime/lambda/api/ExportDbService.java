package com.github.storytime.lambda.api;

import com.github.storytime.lambda.api.common.model.db.DbExport;
import com.github.storytime.lambda.api.common.utils.TimeUtils;
import io.smallrye.common.constraint.NotNull;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static java.time.Instant.now;

@ApplicationScoped
public class ExportDbService {

    @Inject
    Logger logger;


    @Inject
    DynamoDbTable<DbExport> exportTable;

    public Map<String, List<Map<String, String>>> findExport(final @NotNull String userId) {
        final var start = now();

        logger.infof("Looking for export for user: [%s] - started ...", userId);
        Map<String, List<Map<String, String>>> data = exportTable.getItem(Key.builder().partitionValue(userId).build()).getData();

        logger.infof("Found export done for: [%s], time: [%d] - end ...", userId, TimeUtils.timeBetween(start));
        return data;
    }
}
