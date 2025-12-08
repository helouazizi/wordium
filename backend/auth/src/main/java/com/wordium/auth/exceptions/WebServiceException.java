package com.wordium.auth.exceptions;

public class WebServiceException extends RuntimeException {
    private final int status;
    private final String body;


    public WebServiceException(int status, String body) {
        super("Upstream service error: " + status);
        this.status = status;
        this.body = body;

    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }

}
