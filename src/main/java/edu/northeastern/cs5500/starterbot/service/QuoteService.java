package edu.northeastern.cs5500.starterbot.service;

import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageGlobalQuote;

public interface QuoteService extends Service {
    /**
     * Gets a quote for a given symbol; if a quote cannot be obtained, null is returned.
     *
     * <p>See https://www.alphavantage.co/documentation/#latestprice for more information.
     *
     * @param symbol the symbol to get a quote for
     * @return a quote for the given symbol, or null if a quote cannot be obtained
     * @throws AlphaVantageException
     */
    AlphaVantageGlobalQuote getQuote(String symbol) throws RestException, AlphaVantageException;
}
