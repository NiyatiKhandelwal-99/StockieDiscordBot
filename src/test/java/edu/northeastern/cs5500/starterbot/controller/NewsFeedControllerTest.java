package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
public class NewsFeedControllerTest {
    private NewsFeedController getNewsFeedController() {
        NewsFeedController newsFeedController = new NewsFeedController(new FakeNewsFeedService());
        return newsFeedController;
    }

    @Test
    public void testValidTickerSymbols() throws Exception {
        NewsFeedController newsFeedController = getNewsFeedController();
        final String[] validTickerSymbols = {
            "AAPL", "MSFT", "GOOG", "AMZN", "FB", "TSLA", "NFLX", "NVDA", "PYPL", "ADBE"
        };
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(4)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
        for (String tickerSymbol : validTickerSymbols) {
            assertThat(newsFeedController.getNewsFeeds(tickerSymbol, fromTime)).isNotNull();
        }
    }

    @Test
    public void testValidComplexTickerSymbols() throws Exception {
        NewsFeedController newsFeedController = getNewsFeedController();
        final String[] validTickerSymbols = {
            "BTC-USD", "ETH-USD", "ADA-USD", "BRK.A", "BRK.B", "MGC=F", "SIL=F"
        };
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(4)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

        for (String tickerSymbol : validTickerSymbols) {
            assertThat(newsFeedController.getNewsFeeds(tickerSymbol, fromTime)).isNotNull();
        }
    }

    @Test
    public void testInvalidTickerSymbols() throws Exception {
        NewsFeedController newsFeedController = getNewsFeedController();
        final String[] validTickerSymbols = {"BTC-", "ETH-", "-USD", ".A", "BRK.", "=F", "SIL="};
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(4)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
        for (String tickerSymbol : validTickerSymbols) {
            try {
                newsFeedController.getNewsFeeds(tickerSymbol, fromTime);
                fail("Accepted invalid ticker symbol: " + tickerSymbol);
            } catch (BadRequestException e) {
                // expected
            }
        }
    }

    @Test
    public void testGetNewsSentiment() throws Exception {
        NewsFeedController newsFeedController = getNewsFeedController();
        final String[] validTickerSymbols = {
            "AAPL", "MSFT", "GOOG", "AMZN", "FB", "TSLA", "NFLX", "NVDA", "PYPL", "ADBE"
        };
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(4)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
        for (String tickerSymbol : validTickerSymbols) {
            assertThat(newsFeedController.getNewsFeeds(tickerSymbol, fromTime)).isNotNull();
        }
    }
}
