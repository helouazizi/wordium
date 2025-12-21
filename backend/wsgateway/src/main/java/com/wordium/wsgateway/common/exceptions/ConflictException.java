package com.wordium.wsgateway.common.exceptions;
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}