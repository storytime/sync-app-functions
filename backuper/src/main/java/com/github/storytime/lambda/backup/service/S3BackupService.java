package com.github.storytime.lambda.backup.service;

import org.jboss.logging.Logger;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Instant;

import static com.github.storytime.lambda.common.utils.TimeUtils.timeBetween;
import static software.amazon.awssdk.core.sync.RequestBody.fromBytes;

@ApplicationScoped
public class S3BackupService {

    @Inject
    Logger logger;

    @Inject
    S3Client s3Client;

    public void uploadToS3(final String id,
                           final byte[] arrGzip,
                           final String storageClass,
                           final String bucketName,
                           final String s3filePath) {
        final var start = Instant.now();

        logger.infof("S3 data upload for user: [%s], bucket: [%s], file: [%s]  - started ...", id, bucketName, s3filePath);
        final PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .storageClass(storageClass)
                .key(s3filePath)
                .build();

        final PutObjectResponse s3Response = s3Client.putObject(objectRequest, fromBytes(arrGzip));
        logger.infof("S3 upload finished, user: [%s], isSuccessful: [%b], time: [%d]", id, s3Response.sdkHttpResponse().isSuccessful(), timeBetween(start));
    }
}
