package edu.northeastern.cs5500.starterbot.exception.rest;

public class RestException extends Exception {
    private final String message;
    private final int status;

    public RestException(String message, int status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
