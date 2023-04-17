package edu.northeastern.cs5500.starterbot.service.alphavantage;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.NotFoundException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageGlobalQuote;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;

// @EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
// @EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_TESTS_ENABLED", matches = "true")
class AlphaVantageServiceTest {
    private AlphaVantageService getAlphaVantageService() {
        return new AlphaVantageService();
    }

    @Test
    void testGetGlobalQuote() throws Exception {
        assertThat(getAlphaVantageService()).isNotNull();
        final String EXAMPLE_SYMBOL = "AAPL";
        AlphaVantageGlobalQuote quote = getAlphaVantageService().getQuote(EXAMPLE_SYMBOL);
        assertThat(quote).isNotNull();
        assertThat(quote.getSymbol()).isEqualTo("AAPL");
    }

    @Test
    void testGetGlobalQuoteNonexistent() throws RestException {
        assertThat(getAlphaVantageService()).isNotNull();
        final String EXAMPLE_INVALID_SYMBOL = "ASHIOHSAIODHAIOSHDOASHD";
        try {
            getAlphaVantageService().getQuote(EXAMPLE_INVALID_SYMBOL);
            // if we reach this point, we did not throw an expected exception
            fail();
        } catch (NotFoundException e) {
            // expected
        } catch (AlphaVantageException e) {
            // expected
        }
    }

    @Test
    void testGetNewsSentiment() throws RestException, AlphaVantageException {
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(4)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
        final String EXAMPLE_SYMBOL = "AAPL";

        assertThat(getAlphaVantageService()).isNotNull();
        List<AlphaVantageNewsFeed> newsFeeds =
                getAlphaVantageService().getNewsSentiment(EXAMPLE_SYMBOL, fromTime);
        assertThat(newsFeeds).isNotNull();
        assertTrue(newsFeeds.size() > 0);
        assertThat(newsFeeds.get(0).getTitle()).isNotNull();
    }

    @Test
    void testGetNewsSentimentNonexistent() throws RestException, AlphaVantageException {
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(4)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
        final String EXAMPLE_INVALID_SYMBOL = "ASHIOHSAIODHAIOSHDOASHD";

        assertThat(getAlphaVantageService()).isNotNull();
        List<AlphaVantageNewsFeed> newsFeeds =
                getAlphaVantageService().getNewsSentiment(EXAMPLE_INVALID_SYMBOL, fromTime);
        assertThat(newsFeeds).isNull();
    }
}
