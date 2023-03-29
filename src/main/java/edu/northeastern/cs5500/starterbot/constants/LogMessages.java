package edu.northeastern.cs5500.starterbot.constants;

public enum LogMessages {
    EMPTY_TICKER("Received null value for mandatory parameter {}"),
    ERROR_ALPHAVANTAGE_API("Error while calling AlphaVantage API"),
    ERROR_ALPHAVANTAGE_API_REPLY(
            "Unexpected error while calling our quote API, please try again later!"),
    INVALID_TICKER("Invalid ticker passed! Please enter correct ticker.");

    private final String message;

    LogMessages(String message) {
        this.message = message;
    }

    public String getMessage(LogMessages logMessage) {
        return logMessage.message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
