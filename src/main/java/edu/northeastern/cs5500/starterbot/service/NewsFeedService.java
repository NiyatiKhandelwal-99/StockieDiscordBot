package edu.northeastern.cs5500.starterbot.service;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import java.io.IOException;
import java.util.List;

public interface NewsFeedService extends Service {

    /**
     * Returns a list of latest news for a given ticker symbol and time period
     *
     * <p>See https://www.alphavantage.co/documentation/#news-sentiment for more information.</p>
     *
     * @param symbol
     * @param fromTime
     * @return List<AlphaVantageNewsFeed>
     * @throws RestException
     * @throws AlphaVantageException, RestException
     */
    List<AlphaVantageNewsFeed> getNewsSentiment(String symbol, String fromTime)
            throws RestException, AlphaVantageException;

    /**
     * Returns an active list of tickers from an AlphaVantage API
     *
     * <p>See https://www.alphavantage.co/documentation/#listing-status for more information.</p>
     *
     * @param symbol
     * @return List<AlphaVantageBalanceSheet>
     * @throws RestException
     * @throws AlphaVantageException
     */
    List<String> getTickers() throws RestException, AlphaVantageException, IOException;
}
