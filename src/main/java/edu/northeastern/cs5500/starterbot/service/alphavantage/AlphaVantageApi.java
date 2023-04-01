package edu.northeastern.cs5500.starterbot.service.alphavantage;

import java.util.List;

public interface AlphaVantageApi {
    AlphaVantageGlobalQuote getGlobalQuote(String symbol) throws AlphaVantageException;

    List<AlphaVantageNewsFeed> getNewsSentiment(String symbol, String fromTime)
            throws AlphaVantageException;
}
