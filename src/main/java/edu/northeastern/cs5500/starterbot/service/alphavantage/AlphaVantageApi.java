package edu.northeastern.cs5500.starterbot.service.alphavantage;

public interface AlphaVantageApi {
    AlphaVantageGlobalQuote getGlobalQuote(String symbol) throws AlphaVantageException;
}
