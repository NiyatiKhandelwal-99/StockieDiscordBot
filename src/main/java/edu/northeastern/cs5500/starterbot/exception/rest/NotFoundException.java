package edu.northeastern.cs5500.starterbot.exception.rest;

public class NotFoundException extends RestException {
    public NotFoundException() {
        super("Not Found", 404);
    }

    public NotFoundException(String message) {
        super(message, 404);
    }
}
