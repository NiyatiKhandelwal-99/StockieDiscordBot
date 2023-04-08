package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsResponse;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsTopic;
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

    @Test
    public void testAlphaVantageNewsTopicEqualHashCode() {
        AlphaVantageNewsTopic scienceTopic = new AlphaVantageNewsTopic("Science");
        AlphaVantageNewsTopic sameTopic = new AlphaVantageNewsTopic("Science");
        assertThat(scienceTopic.equals(sameTopic)).isTrue();
        assertThat(scienceTopic.getTopic().equals(sameTopic.getTopic())).isTrue();
        assertThat(scienceTopic.hashCode() == sameTopic.hashCode()).isTrue();
        assertThat(scienceTopic.equals(scienceTopic)).isTrue();
        assertThat(scienceTopic.hashCode() == scienceTopic.hashCode()).isTrue();
        assertThat(scienceTopic.toString().equals(sameTopic.toString())).isTrue();
    }

    @Test
    public void testAlphaVantageNewsTopicNotEqualHashCode() {
        AlphaVantageNewsTopic scienceTopic = new AlphaVantageNewsTopic("Science");
        AlphaVantageNewsTopic diffTopic = new AlphaVantageNewsTopic("Technology");
        AlphaVantageNewsTopic emptyTopic = new AlphaVantageNewsTopic("");
        AlphaVantageNewsTopic nullTopic = new AlphaVantageNewsTopic(null);
        assertThat(scienceTopic.equals(diffTopic)).isFalse();
        assertThat(scienceTopic.equals(emptyTopic)).isFalse();
        assertThat(scienceTopic.equals(nullTopic)).isFalse();
        assertThat(scienceTopic.getTopic().equals(diffTopic.getTopic())).isFalse();
        assertThat(scienceTopic.hashCode() == diffTopic.hashCode()).isFalse();
        assertThat(scienceTopic.toString().equals(diffTopic.toString())).isFalse();
        assertThat(scienceTopic.equals("test")).isFalse();
    }

    @Test
    public void testAlphaVantageNewsResponseEqualHashCode() {
        AlphaVantageNewsResponse sourceNewsRes = new AlphaVantageNewsResponse("item", "", "", null);
        AlphaVantageNewsResponse sameNewsRes = new AlphaVantageNewsResponse("item", "", "", null);
        assertThat(sourceNewsRes.equals(sameNewsRes)).isTrue();
        assertThat(sourceNewsRes.getItems().equals(sameNewsRes.getItems())).isTrue();
        assertThat(sourceNewsRes.hashCode() == sameNewsRes.hashCode()).isTrue();
        assertThat(sourceNewsRes.equals(sourceNewsRes)).isTrue();
        assertThat(sourceNewsRes.hashCode() == sourceNewsRes.hashCode()).isTrue();
        assertThat(sourceNewsRes.toString().equals(sourceNewsRes.toString())).isTrue();
    }

    @Test
    public void testAlphaVantageNewsResponseNotEqualHashCode() {
        AlphaVantageNewsResponse sourceNewsRes = new AlphaVantageNewsResponse("item", "", "", null);
        AlphaVantageNewsResponse nullRes = null;
        AlphaVantageNewsResponse emptyNewsRes = new AlphaVantageNewsResponse("", "", "", null);
        assertThat(sourceNewsRes.equals(nullRes)).isFalse();
        assertThat(sourceNewsRes.getItems().equals(emptyNewsRes.getItems())).isFalse();
        assertThat(sourceNewsRes.hashCode() == emptyNewsRes.hashCode()).isFalse();
        assertThat(sourceNewsRes.toString().equals(emptyNewsRes.toString())).isFalse();
        assertThat(sourceNewsRes.equals("test")).isFalse();
    }
}
