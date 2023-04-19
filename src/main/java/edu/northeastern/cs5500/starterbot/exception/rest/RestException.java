package edu.northeastern.cs5500.starterbot.exception.rest;

import lombok.Getter;

public class RestException extends Exception {
    @Getter private final String message;
    @Getter private final int status;

    public RestException(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
