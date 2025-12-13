package com.wordium.auth.dto;

public record FailResponse(
        String message,
        int status,
        Object details) {
}
