package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageGlobalQuote;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageGlobalQuoteResponse;
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

    @Test
    public void testGetNewsSentimentEmptyTicker() throws Exception {
        QuoteController quoteController = getQuoteController();
        String emptyTicker = "";
        try {
            quoteController.getQuote(emptyTicker);
            fail("Accepted invalid ticker symbol: " + emptyTicker);
        } catch (BadRequestException bre) {
            // expected
        }
    }

    @Test
    public void testGetNewsSentimentNullTicker() throws Exception {
        QuoteController quoteController = getQuoteController();
        try {
            quoteController.getQuote(null);
            fail("Got null ticker symbol: ");
        } catch (BadRequestException bre) {
            // expected
        }
    }

    @Test
    void testValidTickerSymbolsEqualsHashCode() throws Exception {
        QuoteController quoteController = getQuoteController();
        final String exampleTicker = "AAPL";
        final String sameTicker = "AAPL";
        AlphaVantageGlobalQuote quote = quoteController.getQuote(exampleTicker);
        AlphaVantageGlobalQuote diffQuote = quoteController.getQuote(sameTicker);
        assertThat(quote.equals(diffQuote)).isTrue();
        assertThat(quote.getSymbol().equals(diffQuote.getSymbol())).isTrue();
        assertThat(quote.getSymbol().hashCode() == diffQuote.getSymbol().hashCode()).isTrue();
        assertThat(quote.equals(quote)).isTrue();
        assertThat(quote.hashCode() == quote.hashCode()).isTrue();
        assertThat(quote.toString().equals(quote.toString())).isTrue();
    }

    @Test
    void testValidTickerSymbolsNotEqualsHashCode() throws Exception {
        QuoteController quoteController = getQuoteController();
        final String exampleTicker = "AAPL";
        final String diffTicker = "GOOG";
        AlphaVantageGlobalQuote quote = quoteController.getQuote(exampleTicker);
        AlphaVantageGlobalQuote diffQuote = quoteController.getQuote(diffTicker);
        assertThat(quote.equals(diffQuote)).isFalse();
        assertThat(quote.getSymbol().equals(diffQuote.getSymbol())).isFalse();
        assertThat(quote.getSymbol().hashCode() == diffQuote.getSymbol().hashCode()).isFalse();
        assertThat(quote.toString().equals(diffQuote.toString())).isFalse();
    }

    @Test
    void testAlphaVantageGlobalQuoteResponseEqualsHashCode() throws Exception {
        AlphaVantageGlobalQuote quote = getQuoteController().getQuote("AAPL");
        AlphaVantageGlobalQuoteResponse globalQuoteResponse =
                new AlphaVantageGlobalQuoteResponse(quote);
        AlphaVantageGlobalQuoteResponse sameQuoteResponse =
                new AlphaVantageGlobalQuoteResponse(quote);

        assertThat(globalQuoteResponse.equals(sameQuoteResponse)).isTrue();
        assertThat(globalQuoteResponse.getGlobalQuote().equals(sameQuoteResponse.getGlobalQuote()))
                .isTrue();
        assertThat(globalQuoteResponse.hashCode() == sameQuoteResponse.hashCode()).isTrue();
        assertThat(globalQuoteResponse.toString().equals(sameQuoteResponse.toString())).isTrue();
    }

    @Test
    void testAlphaVantageGlobalQuoteResponseNotEqualsHashCode() throws Exception {
        AlphaVantageGlobalQuote quote = getQuoteController().getQuote("test");
        AlphaVantageGlobalQuote actualQuote = getQuoteController().getQuote("AAPL");
        AlphaVantageGlobalQuoteResponse globalQuoteResponse =
                new AlphaVantageGlobalQuoteResponse(actualQuote);
        AlphaVantageGlobalQuoteResponse emptyQuoteResponse =
                new AlphaVantageGlobalQuoteResponse(quote);
        AlphaVantageGlobalQuoteResponse nullQuoteResponse = null;

        assertThat(globalQuoteResponse.equals(nullQuoteResponse)).isFalse();
        assertThat(globalQuoteResponse.getGlobalQuote().equals(emptyQuoteResponse.getGlobalQuote()))
                .isFalse();
        assertThat(globalQuoteResponse.hashCode() == emptyQuoteResponse.hashCode()).isFalse();
        assertThat(globalQuoteResponse.toString().equals(emptyQuoteResponse.toString())).isFalse();
    }
}
