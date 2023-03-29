package edu.northeastern.cs5500.starterbot.exception.rest;

public class BadRequestException extends RestException {
    public BadRequestException() {
        super("Bad Request", 400);
    }

    public BadRequestException(String message) {
        super(message, 400);
    }
}
