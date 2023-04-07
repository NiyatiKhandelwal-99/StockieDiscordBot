package edu.northeastern.cs5500.starterbot.service.alphavantage;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import java.util.List;
import java.util.Map;

public class FakeAlphaVantageService implements AlphaVantageApi {

    Map<String, AlphaVantageGlobalQuote> globalQuotes;
    Map<String, List<AlphaVantageNewsFeed>> newsFeed;

    public FakeAlphaVantageService() {
        globalQuotes =
                Map.of(
                        "AAPL",
                        new AlphaVantageGlobalQuote(
                                "AAPL", null, null, null, null, null, null, null, null, null));
        newsFeed =
                Map.of(
                        "AAPL",
                        List.of(
                                new AlphaVantageNewsFeed(
                                        "title", null, null, null, null, null, null, null, null,
                                        null, null, null)));
    }

    @Override
    public AlphaVantageGlobalQuote getGlobalQuote(String symbol) throws AlphaVantageException {
        if ("RAISE_EXCEPTION".equals(symbol)) {
            throw new AlphaVantageException("Raised exception by request");
        }
        return globalQuotes.get(symbol);
    }

    @Override
    public List<AlphaVantageNewsFeed> getNewsSentiment(String symbol, String fromTime)
            throws AlphaVantageException {
        if ("RAISE_EXCEPTION".equals(symbol)) {
            throw new AlphaVantageException("Raised exception by request");
        }
        return newsFeed.get(symbol);
    }
}
