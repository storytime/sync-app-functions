package com.github.storytime.lambda.exporter.common.model.db;

public record User(String id, String zenAuthToken, String lastBackupDate) {
}