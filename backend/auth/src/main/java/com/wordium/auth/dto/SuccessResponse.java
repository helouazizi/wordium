package com.wordium.auth.dto;

public record SuccessResponse<T>(String message, T data) { }