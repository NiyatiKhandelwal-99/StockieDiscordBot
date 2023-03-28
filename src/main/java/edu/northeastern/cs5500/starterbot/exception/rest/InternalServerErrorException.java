package edu.northeastern.cs5500.starterbot.exception.rest;

public class InternalServerErrorException extends RestException {
    public InternalServerErrorException() {
        super("Internal Server Error", 500);
    }

    public InternalServerErrorException(String message) {
        super(message, 500);
    }
}
