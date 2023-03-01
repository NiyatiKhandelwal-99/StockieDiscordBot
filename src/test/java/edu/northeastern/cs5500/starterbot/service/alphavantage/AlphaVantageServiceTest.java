package edu.northeastern.cs5500.starterbot.service.alphavantage;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
class AlphaVantageServiceTest {
    static final String EXAMPLE_SYMBOL = "AAPL";
    static final String EXAMPLE_INVALID_SYMBOL = "ASHIOHSAIODHAIOSHDOASHD";

    private AlphaVantageService getAlphaVantageService() {
        return new AlphaVantageService();
    }

    @Test
    void testGetGlobalQuote() throws AlphaVantageException {
        assertThat(getAlphaVantageService()).isNotNull();
        AlphaVantageGlobalQuote quote = getAlphaVantageService().getGlobalQuote(EXAMPLE_SYMBOL);
        assertThat(quote).isNotNull();
        assertThat(quote.getSymbol()).isEqualTo("AAPL");
    }

    @Test
    void testGetGlobalQuoteNonexistent() throws AlphaVantageException {
        assertThat(getAlphaVantageService()).isNotNull();
        AlphaVantageGlobalQuote quote =
                getAlphaVantageService().getGlobalQuote(EXAMPLE_INVALID_SYMBOL);
        assertThat(quote).isNull();
    }
}
