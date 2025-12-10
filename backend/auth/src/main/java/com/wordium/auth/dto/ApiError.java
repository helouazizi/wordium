package com.wordium.auth.dto;

import java.util.List;

public record ApiError(
        int status,
        String error,
        String message,
        List<FieldValidationError> fieldErrors
) {
        public ApiError(int status, String error, String message) {
                this(status, error, message, null);
        }

        public record FieldValidationError(
                String field,
                String message
        ) {}
}
