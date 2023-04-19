package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import edu.northeastern.cs5500.starterbot.service.NewsFeedService;
import java.io.IOException;
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
        if (symbol.equals("AAPL")) {
            return List.of(
                    new AlphaVantageNewsFeed(
                            "AAPL title1",
                            "url1",
                            fromTime,
                            null,
                            "test summary",
                            "",
                            "test source",
                            "test",
                            "internet",
                            "neutral",
                            "neutral",
                            null),
                    new AlphaVantageNewsFeed(
                            "AAPL title2",
                            "url2",
                            fromTime,
                            null,
                            "test summary",
                            "",
                            "test source",
                            "test",
                            "internet",
                            "neutral",
                            "neutral",
                            null));
        } else {
            return List.of(
                    new AlphaVantageNewsFeed(
                            "title1",
                            "url1",
                            fromTime,
                            null,
                            "test summary",
                            "",
                            "test source",
                            "test",
                            "internet",
                            "neutral",
                            "neutral",
                            null),
                    new AlphaVantageNewsFeed(
                            "title2",
                            "url2",
                            fromTime,
                            null,
                            "test summary",
                            "",
                            "test source",
                            "test",
                            "internet",
                            "neutral",
                            "neutral",
                            null));
        }
    }

    @Override
    public List<String> getTickers() throws RestException, AlphaVantageException, IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTickers'");
    }
}
