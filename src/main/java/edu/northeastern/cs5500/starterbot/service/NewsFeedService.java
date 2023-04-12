package edu.northeastern.cs5500.starterbot.service;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageTickerDetails;
import java.util.List;

public interface NewsFeedService extends Service {

    List<AlphaVantageNewsFeed> getNewsSentiment(String symbol, String fromTime)
            throws RestException, AlphaVantageException;

    List<AlphaVantageTickerDetails> getTicker(String symbol)
            throws RestException, AlphaVantageException;
}
