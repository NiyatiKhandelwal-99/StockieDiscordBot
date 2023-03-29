package edu.northeastern.cs5500.starterbot.exception;

public class MissingRequiredParameterException extends IllegalStateException {
    public MissingRequiredParameterException(String missingParameterName) {
        super("Missing required parameter: " + missingParameterName);
    }
}
