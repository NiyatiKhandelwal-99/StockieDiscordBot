package edu.northeastern.cs5500.starterbot.service.alphavantage;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageGlobalQuote;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import java.util.List;

public interface AlphaVantageApi {
    AlphaVantageGlobalQuote getGlobalQuote(String symbol) throws AlphaVantageException;

    List<AlphaVantageNewsFeed> getNewsSentiment(String symbol, String fromTime)
            throws AlphaVantageException;
}
