package com.wordium.auth.config;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wordium.auth.exceptions.BadRequestException;
import com.wordium.auth.exceptions.ConflictException;
import com.wordium.auth.exceptions.ExternalServiceProblemException;
import com.wordium.auth.exceptions.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ExternalServiceProblemException.class)
        public ProblemDetail handleExternalProblem(ExternalServiceProblemException e) {
                return e.getProblemDetail(); // forwarded EXACTLY
        }

        @ExceptionHandler(NotFoundException.class)
        public ProblemDetail handleNotFound(NotFoundException e) {
                ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                pd.setTitle("Resource Not Found");
                pd.setDetail(e.getMessage());
                return pd;
        }

        @ExceptionHandler(ConflictException.class)
        public ProblemDetail handleConflict(ConflictException e) {
                ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
                pd.setTitle("Conflict");
                pd.setDetail(e.getMessage());
                return pd;
        }

        @ExceptionHandler(BadRequestException.class)
        public ProblemDetail handleBadRequest(BadRequestException e) {
                ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
                pd.setTitle("Bad Request");
                pd.setDetail(e.getMessage());
                return pd;
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ProblemDetail handleValidationErrors(MethodArgumentNotValidException e) {
                ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
                pd.setTitle("Validation Failed");
                pd.setDetail("One or more fields are invalid");

                List<Map<String, String>> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                                .map(err -> Map.of(
                                                "field", err.getField(),
                                                "message",
                                                err.getDefaultMessage() != null ? err.getDefaultMessage()
                                                                : "Invalid value"))
                                .toList();

                pd.setProperty("fieldErrors", fieldErrors);
                return pd;
        }

        @ExceptionHandler(Exception.class)
        public ProblemDetail handleUnexpected(Exception e) {
                ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                pd.setTitle("Internal Server Error");
                pd.setDetail("An unexpected error occurred");
                return pd;
        }
}