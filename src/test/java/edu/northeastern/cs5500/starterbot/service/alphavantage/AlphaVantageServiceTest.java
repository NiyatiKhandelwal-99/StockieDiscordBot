package edu.northeastern.cs5500.starterbot.service.alphavantage;

import org.junit.jupiter.api.Test;

class AlphaVantageServiceTest {
    static final String EXAMPLE_SYMBOL = "AAPL";

    private AlphaVantageService getAlphaVantageService() {
        try {
            return new AlphaVantageService();
        } catch (Exception e) {
            // Environment did not contain an API key, so we skip these tests
            return null;
        }
    }

    @Test
    void testGetGlobalQuote() throws AlphaVantageException {
        // assertThat(getAlphaVantageService()).isNotNull();
        // AlphaVantageGlobalQuote quote = getAlphaVantageService().getGlobalQuote(EXAMPLE_SYMBOL);
        // assertThat(quote.toString()).isEmpty();
    }
}
