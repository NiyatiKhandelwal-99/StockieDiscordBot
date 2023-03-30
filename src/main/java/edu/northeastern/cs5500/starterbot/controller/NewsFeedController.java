package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.NewsFeedService;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageNewsFeed;
import javax.inject.Inject;

public class NewsFeedController {

    NewsFeedService newsFeedService;

    @Inject
    NewsFeedController(NewsFeedService newsFeedService) {
        this.newsFeedService = newsFeedService;
    }

    public AlphaVantageNewsFeed[] getNewsFeeds(String tickerSymbol, String fromTime)
            throws RestException {
        if (tickerSymbol == null || tickerSymbol.isBlank()) {
            throw new BadRequestException("ticker cannot be null or empty");
        }

        tickerSymbol = tickerSymbol.strip().toUpperCase();

        if (!tickerSymbol.matches("^[A-Z]+(?:[.=\\-][A-Z]+)?$")) {
            throw new BadRequestException("ticker had invalid characters");
        }
        return newsFeedService.getNewsSentiment(tickerSymbol, fromTime);
    }
}
