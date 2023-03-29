package edu.northeastern.cs5500.starterbot.service.alphavantage;

import java.util.Map;

public class FakeAlphaVantageService implements AlphaVantageApi {

    Map<String, AlphaVantageGlobalQuote> globalQuotes;

    public FakeAlphaVantageService() {
        globalQuotes =
                Map.of(
                        "AAPL",
                        new AlphaVantageGlobalQuote(
                                "AAPL", null, null, null, null, null, null, null, null, null));
    }

    @Override
    public AlphaVantageGlobalQuote getGlobalQuote(String symbol) throws AlphaVantageException {
        if ("RAISE_EXCEPTION".equals(symbol)) {
            throw new AlphaVantageException("Raised exception by request");
        }
        return globalQuotes.get(symbol);
    }

    @Override
    public AlphaVantageNewsFeed[] getNewsSentiment(String symbol, String fromTime)
            throws AlphaVantageException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNewsSentiment'");
    }
}
