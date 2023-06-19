package com.github.storytime.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.github.storytime.lambda.backup.configs.BackupConfig;
import com.github.storytime.lambda.backup.service.S3BackupService;
import com.github.storytime.lambda.common.model.db.DbUser;
import com.github.storytime.lambda.common.model.req.RequestBody;
import com.github.storytime.lambda.common.service.UserService;
import com.github.storytime.lambda.common.service.ZenRestClientService;
import io.smallrye.common.constraint.NotNull;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static com.github.storytime.lambda.backup.Constant.*;
import static com.github.storytime.lambda.backup.utils.ZipUtils.decodeFromGZIP;
import static com.github.storytime.lambda.backup.utils.ZipUtils.encodeToGZIP;
import static com.github.storytime.lambda.common.utils.TimeUtils.timeBetween;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Instant.now;
import static java.time.format.DateTimeFormatter.ofPattern;

@ApplicationScoped
public class FunctionExportHandler implements RequestHandler<SQSEvent, Integer> {
    @Inject
    Logger logger;
    @Inject
    UserService userService;
    @Inject
    @RestClient
    ZenRestClientService zenRestClientService;
    @Inject
    S3BackupService s3Service;
    @Inject
    BackupConfig backupConfig;

    @Override
    public Integer handleRequest(final @NotNull SQSEvent message, final Context context) {
        final var reqId = context.getAwsRequestId();
        final Instant lambdaStart = now();

        try {
            logger.infof("====== Starting backup, lambdaStart: [%d], reqId: [%s], msg: [%s]", lambdaStart.getEpochSecond(), reqId, message);
            final String sqsMessage = message.getRecords()
                    .stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Cannot get SQS message"))
                    .getBody();

            final DbUser user = userService.findUserById(sqsMessage);
            final RequestBody body = new RequestBody(lambdaStart.getEpochSecond(), backupConfig.getStartFrom(), new HashSet<>());
            final String authToken = BEARER + SPACE + user.getZenAuthToken().trim();
            final String zenData = zenRestClientService.getDiff(authToken, body);

            byte[] original = zenData.getBytes(UTF_8);
            byte[] arrGzip = encodeToGZIP(original);
            byte[] fromBase64Gzip = decodeFromGZIP(arrGzip);

            logger.infof("Backup data size, org: [%d], bin: [%d], gzip: [%d], reqId: [%s], verifying backups.....", zenData.length(), original.length, arrGzip.length, reqId);
            boolean isValidBinary = Arrays.equals(original, fromBase64Gzip);
            logger.infof("Is backups valid bin: [%b], reqId: [%s], going to upload S3 ...", isValidBinary, reqId);

            final String s3filePath = user.getId() + SLASH + LocalDateTime.now().format(ofPattern(backupConfig.getDateFormat())) + JSON_GZ; // 2022-08-27-07-53-08.json.gz
            s3Service.uploadToS3(user.getId(), arrGzip, backupConfig.getStorageClass(), backupConfig.getBucket(), s3filePath);

            logger.infof("====== Finished backup, done for user: [%s], time: [%d], reqId: [%s]", user.getId(), timeBetween(lambdaStart), reqId);
            return HttpStatus.SC_OK;
        } catch (final Exception ex) {
            logger.errorf("Error in lambda, error: [%s], time: [%d], reqId: [%s]", ex.getMessage(), timeBetween(lambdaStart), reqId, ex);
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }
}