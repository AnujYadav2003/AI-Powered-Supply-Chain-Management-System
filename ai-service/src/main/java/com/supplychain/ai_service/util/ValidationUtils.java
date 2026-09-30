package com.supplychain.ai_service.util;

import com.supplychain.ai_service.exception.ToolExecutionException;

import java.util.UUID;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static String requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ToolExecutionException(
                    "INVALID_REQUEST", fieldName + " must not be empty."
            );
        }
        return value.trim();
    }

    public static UUID parseUuid(String value, String fieldName) {
        try {
            return UUID.fromString(requireNotBlank(value, fieldName));
        } catch (IllegalArgumentException e) {
            throw new ToolExecutionException(
                    "INVALID_UUID", fieldName + " must be a valid UUID."
            );
        }
    }
}