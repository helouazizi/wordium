package com.wordium.auth.config;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wordium.auth.dto.ApiError;
import com.wordium.auth.exceptions.BadRequestException;
import com.wordium.auth.exceptions.ConflictException;
import com.wordium.auth.exceptions.ExternalServiceException;
import com.wordium.auth.exceptions.NotFoundException;
import com.wordium.auth.exceptions.ServerException;
import com.wordium.auth.exceptions.UnauthorizedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException e) {
        ApiError error = new ApiError(
                404,
                "Not Found",
                e.getMessage()
        );
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException e) {
        ApiError error = new ApiError(
                409,
                "Conflict",
                e.getMessage()
        );
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException e) {
        ApiError error = new ApiError(
                401,
                "Unauthorized",
                e.getMessage()
        );
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException e) {
        ApiError error = new ApiError(
                400,
                "Bad Request",
                e.getMessage()
        );
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ApiError> handleServerError(ServerException e) {
        ApiError error = new ApiError(
                500,
                "Internal Server Error",
                e.getMessage()
        );
        return ResponseEntity.status(500).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException e) {

        List<ApiError.FieldValidationError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ApiError.FieldValidationError(
                        err.getField(),
                        err.getDefaultMessage()))
                .toList();

        ApiError error = new ApiError(
                400,
                "Validation Failed",
                "One or more fields are invalid",
                fieldErrors
        );

        return ResponseEntity.status(400).body(error);
    }

    /**
     * Handles errors forwarded from the Users Service
     * (e.g., 422 Unprocessable Entity)
     */
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiError> handleExternalServiceException(ExternalServiceException e) {

        ApiError error = new ApiError(
                e.getStatus(),
                e.getError(),
                e.getMessage(),
                e.getFieldErrors() // can be null
        );

        return ResponseEntity.status(e.getStatus()).body(error);
    }
}
