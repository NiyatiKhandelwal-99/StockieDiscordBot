package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageTickerDetails;
import edu.northeastern.cs5500.starterbot.service.NewsFeedService;
import java.util.List;
import javax.inject.Inject;

public class NewsFeedController {

    NewsFeedService newsFeedService;

    @Inject
    NewsFeedController(NewsFeedService newsFeedService) {
        this.newsFeedService = newsFeedService;
    }

    public List<AlphaVantageNewsFeed> getNewsFeeds(String tickerSymbol, String fromTime)
            throws RestException, AlphaVantageException {
        if (tickerSymbol == null || tickerSymbol.isBlank()) {
            throw new BadRequestException("ticker cannot be null or empty");
        }

        tickerSymbol = tickerSymbol.strip().toUpperCase();

        if (!tickerSymbol.matches("^[A-Z]+(?:[.=\\-][A-Z]+)?$")) {
            throw new BadRequestException("ticker had invalid characters");
        }
        return newsFeedService.getNewsSentiment(tickerSymbol, fromTime);
    }

    public List<AlphaVantageTickerDetails> getTicker(String tickerSymbol)
            throws RestException, AlphaVantageException {
        if (tickerSymbol == null || tickerSymbol.isBlank()) {
            throw new BadRequestException("ticker cannot be null or empty");
        }

        tickerSymbol = tickerSymbol.strip().toUpperCase();

        if (!tickerSymbol.matches("^[A-Z]+(?:[.=\\-][A-Z]+)?$")) {
            throw new BadRequestException("ticker had invalid characters");
        }
        return newsFeedService.getTicker(tickerSymbol);
    }
}
