package com.wordium.users.dto;

public record SuccessResponse<T>(String message, T data) {
}
