package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

// @EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
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

    @Test
    public void testGetNewsSentimentEmptyTicker() throws Exception {
        NewsFeedController newsFeedController = getNewsFeedController();
        String emptyTicker = "";
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(4)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
        try {
            newsFeedController.getNewsFeeds(emptyTicker, fromTime);
            fail("Accepted invalid ticker symbol: " + emptyTicker);
        } catch (BadRequestException bre) {
            // expected
        }
    }

    @Test
    public void testGetNewsSentimentNullTicker() throws Exception {
        NewsFeedController newsFeedController = getNewsFeedController();
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(4)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
        try {
            String nullTicker = null;
            newsFeedController.getNewsFeeds(nullTicker, fromTime);
            fail("Accepted invalid ticker symbol: " + nullTicker);
        } catch (BadRequestException bre) {
            // expected
        }
    }

    @Test
    public void testGetNewsSentimentEqualHashCode() throws Exception {
        NewsFeedController newsFeedController = getNewsFeedController();
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(4)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
        final String exampleTicker = "AAPL";
        List<AlphaVantageNewsFeed> newsFeedSameTicker1 =
                newsFeedController.getNewsFeeds(exampleTicker, fromTime);
        List<AlphaVantageNewsFeed> newsFeedSameTicker2 =
                newsFeedController.getNewsFeeds(exampleTicker, fromTime);
        assertThat(newsFeedSameTicker1.equals(newsFeedSameTicker2)).isTrue();
        assertThat(
                        newsFeedSameTicker1
                                .get(0)
                                .getTitle()
                                .equals(newsFeedSameTicker2.get(0).getTitle()))
                .isTrue();
        assertThat(
                        newsFeedSameTicker1.get(0).getTitle().hashCode()
                                == newsFeedSameTicker2.get(0).getTitle().hashCode())
                .isTrue();
        assertThat(newsFeedSameTicker1.equals(newsFeedSameTicker1)).isTrue();
        assertThat(newsFeedSameTicker1.hashCode() == newsFeedSameTicker1.hashCode()).isTrue();
        assertThat(newsFeedSameTicker1.toString().equals(newsFeedSameTicker2.toString())).isTrue();
    }

    @Test
    public void testGetNewsSentimentNotEqualHashCode() throws Exception {
        NewsFeedController newsFeedController = getNewsFeedController();
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(4)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
        final String exampleTicker = "AAPL";
        final String differentTicker = "GOOG";
        List<AlphaVantageNewsFeed> newsFeedSameTicker1 =
                newsFeedController.getNewsFeeds(exampleTicker, fromTime);
        List<AlphaVantageNewsFeed> newsFeedDiffTicker =
                newsFeedController.getNewsFeeds(differentTicker, fromTime);
        assertThat(newsFeedSameTicker1.equals(newsFeedDiffTicker)).isFalse();
        assertThat(
                        newsFeedSameTicker1
                                .get(0)
                                .getTitle()
                                .equals(newsFeedDiffTicker.get(0).getTitle()))
                .isFalse();
        assertThat(newsFeedSameTicker1.hashCode() == newsFeedDiffTicker.hashCode()).isFalse();

        List<AlphaVantageNewsFeed> newsFeedEmpty = new ArrayList<>();
        List<AlphaVantageNewsFeed> newsFeedNull = null;
        assertThat(newsFeedSameTicker1.equals(newsFeedEmpty)).isFalse();
        assertThat(newsFeedSameTicker1.equals(newsFeedNull)).isFalse();
        assertThat(newsFeedSameTicker1.toString().equals("test")).isFalse();
    }
}
