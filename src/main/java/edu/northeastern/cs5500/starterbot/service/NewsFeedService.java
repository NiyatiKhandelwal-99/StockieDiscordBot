package edu.northeastern.cs5500.starterbot.service;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface NewsFeedService extends Service {

    List<AlphaVantageNewsFeed> getNewsSentiment(String symbol, String fromTime)
            throws RestException, AlphaVantageException;

    Map<String, String> getTickers() throws RestException, AlphaVantageException, IOException;
}
