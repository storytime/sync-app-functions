package com.github.storytime.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.github.storytime.lambda.common.model.db.DbUser;
import com.github.storytime.lambda.common.service.UserService;
import com.github.storytime.lambda.stater.StarterConfig;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequestEntry;

import javax.inject.Inject;
import java.util.List;

import static com.github.storytime.lambda.common.utils.TimeUtils.timeBetween;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static software.amazon.awssdk.services.sqs.model.SendMessageBatchRequestEntry.builder;

@RegisterForReflection
public class FunctionStarterHandler implements RequestHandler<ScheduledEvent, Integer> {
    @Inject
    Logger logger;
    @Inject
    SqsClient sqsClient;
    @Inject
    UserService userService;

    @Inject
    StarterConfig starterConfig;

    @Override
    public Integer handleRequest(final ScheduledEvent input, final Context context) {
        final var lambdaStart = now();
        final var reqId = context.getAwsRequestId();
        logger.infof("Starting, reqId: [%s] - started...", reqId);

        final PageIterable<DbUser> allUsersIds = userService.findAll();
        final List<SendMessageBatchRequestEntry> entriesToSend = allUsersIds.items().stream()
                .map(user -> builder().id(randomUUID().toString()).messageBody(user.getId()).build())
                .toList();

        final SendMessageBatchRequest batchSqsRequest = SendMessageBatchRequest.builder()
                .entries(entriesToSend)
                .queueUrl(starterConfig.getQueueUrl())
                .build();

        final var sqsStart = now();
        logger.debugf("Started to sent sqs msg, reqId: [%s]", reqId);
        sqsClient.sendMessageBatch(batchSqsRequest);

        logger.infof("Finished lambda, entries: [%d], sqs time: [%d], total time: [%d], reqId: [%s]", entriesToSend.size(), timeBetween(sqsStart), timeBetween(lambdaStart), reqId);
        return 0;
    }
}