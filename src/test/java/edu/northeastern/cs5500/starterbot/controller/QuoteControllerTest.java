package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import org.junit.jupiter.api.Test;

class QuoteControllerTest {
    private QuoteController getQuoteController() {
        QuoteController quoteController = new QuoteController(new FakeQuoteService());
        return quoteController;
    }

    @Test
    void testValidTickerSymbols() throws Exception {
        QuoteController quoteController = getQuoteController();
        final String[] validTickerSymbols = {
            "AAPL", "MSFT", "GOOG", "AMZN", "FB", "TSLA", "NFLX", "NVDA", "PYPL", "ADBE"
        };

        for (String tickerSymbol : validTickerSymbols) {
            assertThat(quoteController.getQuote(tickerSymbol)).isNotNull();
        }
    }

    @Test
    void testValidComplexTickerSymbols() throws Exception {
        QuoteController quoteController = getQuoteController();
        final String[] validTickerSymbols = {
            "BTC-USD", "ETH-USD", "ADA-USD", "BRK.A", "BRK.B", "MGC=F", "SIL=F"
        };

        for (String tickerSymbol : validTickerSymbols) {
            assertThat(quoteController.getQuote(tickerSymbol)).isNotNull();
        }
    }

    @Test
    void testInvalidTickerSymbols() throws Exception {
        QuoteController quoteController = getQuoteController();
        final String[] validTickerSymbols = {"BTC-", "ETH-", "-USD", ".A", "BRK.", "=F", "SIL="};

        for (String tickerSymbol : validTickerSymbols) {
            try {
                quoteController.getQuote(tickerSymbol);
                fail("Accepted invalid ticker symbol: " + tickerSymbol);
            } catch (BadRequestException e) {
                // expected
            }
        }
    }
}
