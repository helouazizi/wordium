package com.wordium.users.exceptions;

public class NotFoundException extends RuntimeException {

    private String resource;
    private String field;
    private Object value;

    public NotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: '%s'", resource, field, value));
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    public NotFoundException(String message) {
        super(message);
    }

    public String getResource() {
        return resource;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}
