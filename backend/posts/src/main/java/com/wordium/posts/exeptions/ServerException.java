package com.wordium.posts.exeptions;

public class ServerException extends RuntimeException {

    public ServerException(String message) {
        super(message);
    }
}
