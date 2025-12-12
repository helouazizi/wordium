package com.wordium.users.config;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wordium.users.dto.FailResponse;
import com.wordium.users.exceptions.BadRequestException;
import com.wordium.users.exceptions.ConflictException;
import com.wordium.users.exceptions.NotFoundException;
import com.wordium.users.exceptions.ServerException;
import com.wordium.users.exceptions.UnauthorizedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<FailResponse> handleNotFound(NotFoundException e) {
        FailResponse error = new FailResponse(404, "Not Found", e.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<FailResponse> handleConflict(ConflictException e) {
        FailResponse error = new FailResponse(409, "Conflict", e.getMessage());
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<FailResponse> handleUnauthorized(UnauthorizedException e) {
        FailResponse error = new FailResponse(401, "Unauthorized", e.getMessage());
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<FailResponse> handleBadRequest(BadRequestException e) {
        FailResponse error = new FailResponse(400, "Bad Request", e.getMessage());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<FailResponse> handleServerError(ServerException e) {
        FailResponse error = new FailResponse(500, "Internal Server Error", e.getMessage());
        return ResponseEntity.status(500).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailResponse> handleInputsErrors(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        err -> err.getField(), 
                        err -> err.getDefaultMessage(), 
                        (existing, replacement) -> existing // handle duplicate keys
                ));

        FailResponse response = new FailResponse(
                400,
                "Validation failed",
                errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
