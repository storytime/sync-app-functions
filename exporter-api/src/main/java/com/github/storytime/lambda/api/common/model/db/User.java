package com.github.storytime.lambda.api.common.model.db;

public record User(String id, String zenAuthToken, String lastBackupDate) {
}