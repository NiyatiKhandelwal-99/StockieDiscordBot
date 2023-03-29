package edu.northeastern.cs5500.starterbot.service.alphavantage;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import edu.northeastern.cs5500.starterbot.exception.rest.NotFoundException;

@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
class AlphaVantageServiceTest {
    static final String EXAMPLE_SYMBOL = "AAPL";
    static final String EXAMPLE_INVALID_SYMBOL = "ASHIOHSAIODHAIOSHDOASHD";

    private AlphaVantageService getAlphaVantageService() {
        return new AlphaVantageService();
    }

    @Test
    void testGetGlobalQuote() throws Exception {
        assertThat(getAlphaVantageService()).isNotNull();
        AlphaVantageGlobalQuote quote = getAlphaVantageService().getQuote(EXAMPLE_SYMBOL);
        assertThat(quote).isNotNull();
        assertThat(quote.getSymbol()).isEqualTo("AAPL");
    }

    @Test
    void testGetGlobalQuoteNonexistent() throws Exception {
        assertThat(getAlphaVantageService()).isNotNull();
        try {
            getAlphaVantageService().getQuote(EXAMPLE_INVALID_SYMBOL);
            // if we reach this point, we did not throw an expected exception
            fail();
        } catch (NotFoundException e) {
            // expected
        }
    }
}
