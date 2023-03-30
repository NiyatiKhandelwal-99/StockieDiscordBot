package edu.northeastern.cs5500.starterbot.service.alphavantage;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import edu.northeastern.cs5500.starterbot.exception.rest.NotFoundException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
// @EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_TESTS_ENABLED", matches = "true")
class AlphaVantageServiceTest {
    static final String EXAMPLE_SYMBOL = "AAPL";
    static final String EXAMPLE_INVALID_SYMBOL = "ASHIOHSAIODHAIOSHDOASHD";
    private static final int NUMBER_OF_DAYS = 4;

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
    void testGetGlobalQuoteNonexistent() throws RestException {
        assertThat(getAlphaVantageService()).isNotNull();
        try {
            getAlphaVantageService().getQuote(EXAMPLE_INVALID_SYMBOL);
            // if we reach this point, we did not throw an expected exception
            fail();
        } catch (NotFoundException e) {
            // expected
        } catch (AlphaVantageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    void testGetNewsSentiment() throws RestException {
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(NUMBER_OF_DAYS)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

        assertThat(getAlphaVantageService()).isNotNull();
        AlphaVantageNewsFeed[] newsFeeds =
                getAlphaVantageService().getNewsSentiment(EXAMPLE_SYMBOL, fromTime);
        assertThat(newsFeeds).isNotNull();
        assertTrue(newsFeeds.length > 0);
        assertThat(newsFeeds[0].getTitle()).isNotNull();
    }

    @Test
    void testGetNewsSentimentNonexistent() throws RestException {
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(NUMBER_OF_DAYS)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

        assertThat(getAlphaVantageService()).isNotNull();
        AlphaVantageNewsFeed[] newsFeeds =
                getAlphaVantageService().getNewsSentiment(EXAMPLE_INVALID_SYMBOL, fromTime);
        assertThat(newsFeeds).isNull();
    }
}
