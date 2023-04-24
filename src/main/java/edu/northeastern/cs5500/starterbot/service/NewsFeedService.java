package edu.northeastern.cs5500.starterbot.service;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import java.io.IOException;
import java.util.List;

public interface NewsFeedService extends Service {

    /**
     * Gets a quote for a given symbol; if a quote cannot be obtained, null is returned.
     *
     * <p>See https://www.alphavantage.co/documentation/#news-sentiment for more information.
     *
     * @param symbol the symbol to get a quote for
     * @return a quote for the given symbol, or null if a quote cannot be obtained
     * @throws AlphaVantageException
     */
    List<AlphaVantageNewsFeed> getNewsSentiment(String symbol, String fromTime)
            throws RestException, AlphaVantageException;

    /**
     * Gets a quote for a given symbol; if a quote cannot be obtained, null is returned.
     *
     * <p>See https://www.alphavantage.co/documentation/#listing-status for more information.
     */
    List<String> getTickers() throws RestException, AlphaVantageException, IOException;
}
