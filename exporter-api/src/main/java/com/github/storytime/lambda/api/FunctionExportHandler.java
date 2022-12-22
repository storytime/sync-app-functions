package com.github.storytime.lambda.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.github.storytime.lambda.api.common.model.db.User;
import com.github.storytime.lambda.api.common.service.UserService;
import com.github.storytime.lambda.api.common.utils.TimeUtils;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.Instant;

import static java.time.Instant.now;

public class FunctionExportHandler implements RequestHandler<SQSEvent, Void> {

    @Inject
    UserService userService;
    @Inject
    Logger logger;


    @Override
    public Void handleRequest(final @NotNull SQSEvent message, Context context) {
        final Instant lambdaStart = now();
        final var reqId = context.getAwsRequestId();

        logger.infof("====== Starting exporter, lambdaStart: [%d], reqId: [%s], msg: [%s]", lambdaStart.getEpochSecond(), reqId, message);
        final String userId = message.getRecords()
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot get SQS message"))
                .getBody();

        final User user = userService.findUserById(userId);

        logger.infof("====== Finished export, done for user: [%s], time: [%d], reqId: [%s]", user.id(), TimeUtils.timeBetween(lambdaStart), reqId);
        return null;

    }
}
