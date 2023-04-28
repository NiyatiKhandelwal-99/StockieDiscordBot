package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageGlobalQuote;
import edu.northeastern.cs5500.starterbot.service.QuoteService;
import javax.inject.Inject;

/** This controller class is used to connect with AlphaVantage APIs and price commands. */
public class QuoteController {

    QuoteService quoteService;

    @Inject
    QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    /**
     * Returns a current pricing of a given ticker symbol
     *
     * @param tickerSymbol
     * @return AlphaVantageGlobalQuote
     * @throws RestException
     * @throws AlphaVantageException
     */
    public AlphaVantageGlobalQuote getQuote(String tickerSymbol)
            throws RestException, AlphaVantageException {
        if (tickerSymbol == null || tickerSymbol.isBlank()) {
            throw new BadRequestException("ticker cannot be null or empty");
        }

        tickerSymbol = tickerSymbol.strip().toUpperCase();

        if (!tickerSymbol.matches("^[A-Z]+(?:[.=\\-][A-Z]+)?$")) {
            throw new BadRequestException("ticker had invalid characters");
        }

        return quoteService.getQuote(tickerSymbol);
    }
}
