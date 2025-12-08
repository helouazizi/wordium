package com.wordium.users.exceptions;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}