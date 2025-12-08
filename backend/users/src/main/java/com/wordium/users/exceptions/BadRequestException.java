package com.wordium.users.exceptions;


public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
