package com.github.storytime.lambda.api;

import com.github.storytime.lambda.common.model.db.DbExport;
import com.github.storytime.lambda.common.utils.TimeUtils;
import io.smallrye.common.constraint.NotNull;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

import static com.github.storytime.lambda.common.utils.TimeUtils.timeBetween;
import static java.time.Instant.now;
import static software.amazon.awssdk.enhanced.dynamodb.Key.builder;

@ApplicationScoped
public class ExportDbService {

    @Inject
    Logger logger;

    @Inject
    DynamoDbTable<DbExport> exportTable;

    public Map<Integer, String>  findExport(final @NotNull String userId) {
        final var start = now();

        logger.infof("Looking for export for user: [%s] - started ...", userId);
        Map<Integer, String>  data = exportTable.getItem(builder().partitionValue(userId).build()).getData();

        logger.infof("Found export done for: [%s], time: [%d] - end ...", userId, timeBetween(start));
        return data;
    }
}
