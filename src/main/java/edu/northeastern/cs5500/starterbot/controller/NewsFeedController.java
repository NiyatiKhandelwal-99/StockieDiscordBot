package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import edu.northeastern.cs5500.starterbot.service.NewsFeedService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * This controller class is used to connect with AlphaVantage APIs and news commands.
 */
public class NewsFeedController {

    NewsFeedService newsFeedService;

    @Inject
    NewsFeedController(NewsFeedService newsFeedService) {
        this.newsFeedService = newsFeedService;
    }

    
    /** 
     * Returns a list of news feeds for a given ticker symbol and time period.
     * @param tickerSymbol
     * @param fromTime
     * @return List<AlphaVantageNewsFeed>
     * @throws RestException
     * @throws AlphaVantageException
     */
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

    
    /** 
     * Returns a map of active tickers from an AlphaVantage API.
     * @return Map<String, String>
     * @throws RestException
     * @throws AlphaVantageException
     * @throws IOException
     */
    public Map<String, String> getTickers()
            throws RestException, AlphaVantageException, IOException {
        List<String> uniqueTickers = newsFeedService.getTickers();
        Map<String, String> uniqueTickersMap = new HashMap<>();
        for (String tickerDetail : uniqueTickers) {
            String[] tickerSymbolAndName = tickerDetail.split(",");
            uniqueTickersMap.put(tickerSymbolAndName[0], tickerSymbolAndName[1]);
        }
        return uniqueTickersMap;
    }
}
