package com.wordium.auth.exceptions;

import java.util.List;

import com.wordium.auth.dto.ApiError;

public class ExternalServiceException extends RuntimeException {

    private final int status;
    private final String error;
    private final List<ApiError.FieldValidationError> fieldErrors;

    public ExternalServiceException(
            int status,
            String error,
            String message,
            List<ApiError.FieldValidationError> fieldErrors
    ) {
        super(message); 
        this.status = status;
        this.error = error;
        this.fieldErrors = fieldErrors;
    }

    public int getStatus() { return status; }

    public String getError() { return error; }

    public List<ApiError.FieldValidationError> getFieldErrors() { return fieldErrors; }
}
