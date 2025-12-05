package com.wordium.auth.config;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wordium.auth.dto.ApiError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 401 Unauthorized
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiError> handleUnauthorized(SecurityException e) {
        ApiError error = new ApiError(
                401,
                "Unauthorized",
                e.getMessage(),
                Instant.now());
        return ResponseEntity.status(401).body(error);
    }

    // 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException e) {
        ApiError error = new ApiError(
                400,
                "Bad Request",
                e.getMessage(),
                Instant.now());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleNotFound(RuntimeException e) {
        if (e.getMessage() != null && e.getMessage().toLowerCase().contains("not found")) {
            ApiError error = new ApiError(
                    404,
                    "Not Found",
                    e.getMessage(),
                    Instant.now());
            return ResponseEntity.status(404).body(error);
        }

        return handleServerError(e); // fallback
    }

    // 409 Conflict
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleConflict(IllegalStateException e) {
        ApiError error = new ApiError(
                409,
                "Conflict",
                e.getMessage(),
                Instant.now());

        return ResponseEntity.status(409).body(error);
    }

    // 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleServerError(Exception e) {
        ApiError error = new ApiError(
                500,
                "Internal Server Error",
                e.getMessage(),
                Instant.now());
        return ResponseEntity.status(500).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }

        ApiError error = new ApiError(
                400,
                "Validation Failed",
                errors.toString(),
                Instant.now());
        return ResponseEntity.status(400).body(error);
    }

}
