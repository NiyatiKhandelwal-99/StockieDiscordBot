package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.NewsFeedService;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageNewsFeed;
import java.util.List;

public class FakeNewsFeedService implements NewsFeedService {

    public FakeNewsFeedService() {
        // empty constructor for testing
    }

    @Override
    public void register() {
        // Not required to do anything; not doing anything.
    }

    @Override
    public List<AlphaVantageNewsFeed> getNewsSentiment(String symbol, String fromTime)
            throws RestException, AlphaVantageException {
        return List.of(
                new AlphaVantageNewsFeed(
                        fromTime, fromTime, fromTime, null, fromTime, fromTime, fromTime, fromTime,
                        fromTime, symbol, fromTime, null),
                new AlphaVantageNewsFeed(
                        fromTime, fromTime, fromTime, null, fromTime, fromTime, fromTime, fromTime,
                        fromTime, symbol, fromTime, null));
    }
}
