package com.github.storytime.lambda.exporter.model;

public record ExportTransaction(String id, Double amount, String category, String date) {
}
