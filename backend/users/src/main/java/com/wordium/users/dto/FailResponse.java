package com.wordium.users.dto;

public record FailResponse(
        int status,
        String message,
        Object details) {

}
