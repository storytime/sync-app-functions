package com.github.storytime.lambda.exporter.service;


import com.github.storytime.lambda.common.model.db.DbExport;
import com.github.storytime.lambda.common.model.db.DbUser;
import io.smallrye.common.constraint.NotNull;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

import static com.github.storytime.lambda.common.utils.TimeUtils.timeBetween;
import static java.time.Instant.now;

@ApplicationScoped
public class ExportDbService {

    @Inject
    Logger logger;

    @Inject
    DynamoDbTable<DbExport> exportTable;


    public void saveExport(final @NotNull DbUser user,
                           final @NotEmpty Map<Integer, String> exportData) {
        final var start = now();
        final var id = user.getId();
        logger.infof("Saving export for user: [%s] - started ...", id);
        exportTable.putItem(new DbExport(id, exportData));
        logger.infof("Saved export, user: [%s], time: [%d]", id, timeBetween(start));
    }
}
