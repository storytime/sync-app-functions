package com.github.storytime.lambda.backup.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class ZipUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ZipUtils.class);

    private ZipUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static byte[] decodeFromGZIP(final byte[] arr) {
        try {
            final ByteArrayInputStream bais = new ByteArrayInputStream(arr);
            final GZIPInputStream gzip = new GZIPInputStream(bais);
            return gzip.readAllBytes();
        } catch (Exception e) {
            LOG.error("Cannot encode from gzip, error: [{}]", e.getMessage(), e);
            throw new IllegalArgumentException("Cannot encode from gzip");
        }
    }

    public static byte[] encodeToGZIP(final byte[] arr) {
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final GZIPOutputStream gzip = new GZIPOutputStream(baos);
            gzip.write(arr);
            gzip.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            LOG.error("Cannot encode to gzip, error: [{}]", e.getMessage(), e);
            throw new IllegalArgumentException("Cannot encode to gzip");
        }
    }
}
