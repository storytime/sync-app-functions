package com.github.storytime.lambda.exporter.common.model.req;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Set;

@RegisterForReflection
public record RequestBody(long currentClientTimestamp, long serverTimestamp, Set<String> forceFetch) {
}