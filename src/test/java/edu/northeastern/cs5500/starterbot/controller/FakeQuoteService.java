package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.QuoteService;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageGlobalQuote;

public class FakeQuoteService implements QuoteService {

    public FakeQuoteService() {
        // empty constructor for testing
    }

    @Override
    public void register() {
        // Not required to do anything; not doing anything.
    }

    @Override
    public AlphaVantageGlobalQuote getQuote(String symbol) throws RestException {
        return new AlphaVantageGlobalQuote(
                symbol, symbol, symbol, symbol, symbol, symbol, symbol, symbol, symbol, symbol);
    }
}
