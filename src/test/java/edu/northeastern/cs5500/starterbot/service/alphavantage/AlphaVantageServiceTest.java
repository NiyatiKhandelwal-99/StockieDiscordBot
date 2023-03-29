package edu.northeastern.cs5500.starterbot.service.alphavantage;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_TESTS_ENABLED", matches = "true")
class AlphaVantageServiceTest {
    static final String EXAMPLE_SYMBOL = "AAPL";
    static final String EXAMPLE_INVALID_SYMBOL = "ASHIOHSAIODHAIOSHDOASHD";
    private static final int NUMBER_OF_DAYS = 4;

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

    @Test
    void testGetNewsSentiment() throws AlphaVantageException {
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
    void testGetNewsSentimentNonexistent() throws AlphaVantageException {
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
