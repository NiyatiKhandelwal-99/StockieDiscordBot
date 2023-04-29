package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageWinningPortfolios;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageWinningPortfoliosRankings;
import edu.northeastern.cs5500.starterbot.service.WinningPortfoliosService;
import java.util.List;

public class FakeWinningPortfoliosService implements WinningPortfoliosService {

    public FakeWinningPortfoliosService() {}

    @Override
    public void register() {
        // Not required to do anything;
    }

    @Override
    public List<AlphaVantageWinningPortfoliosRankings> getWinningPortfolios(String date)
            throws RestException, AlphaVantageException {
        return List.of(
                new AlphaVantageWinningPortfoliosRankings(
                        "1",
                        List.of(
                                new AlphaVantageWinningPortfolios("AAPL", "100"),
                                new AlphaVantageWinningPortfolios("GOOG", "10")),
                        "2021-11-12",
                        "2021-11-26",
                        "28.48723",
                        "2036.0",
                        "2616.0"),
                new AlphaVantageWinningPortfoliosRankings(
                        "2",
                        List.of(
                                new AlphaVantageWinningPortfolios("MSFT", "5"),
                                new AlphaVantageWinningPortfolios("TSLA", "1000")),
                        "2021-11-12",
                        "2021-11-26",
                        "20.92809",
                        "105313.0",
                        "127353.0"));
    }
}
