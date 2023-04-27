package edu.northeastern.cs5500.starterbot.constants;

public abstract class LogMessages {
    public static final String EMPTY_TICKER = "Received null value for mandatory parameter";
    public static final String ERROR_ALPHAVANTAGE_API = "Error while calling AlphaVantage API";
    public static final String ERROR_YAHOOFINANCE_API = "Error while calling Yahoo Finance API";
    public static final String ERROR_ALPHAVANTAGE_API_REPLY =
            "Unexpected error while calling AlhpaVantage API, please try again later! %s";
    public static final String ERROR_YAHOOFINANCE_API_REPLY =
            "Unexpected error while calling yahoo API, please try again later! %s";
    public static final String INVALID_TICKER =
            "Invalid ticker passed! Please enter correct ticker.";
    public static final String EMPTY_RESPONSE = "No response found for %s";
}
