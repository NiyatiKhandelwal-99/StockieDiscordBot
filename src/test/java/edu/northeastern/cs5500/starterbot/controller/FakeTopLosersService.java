package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.YahooFinanceException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.TopLosersService;
import java.util.Map;

public class FakeTopLosersService implements TopLosersService {

    @Override
    public void register() {}

    @Override
    public Map<String, String> getTopLosers() throws RestException, YahooFinanceException {
        return Map.of(
                "AAPL", "-10.45", "GOOG", "-32.7", "TSLA", "-100.21", "MSFT", "-23.4", "AMZN",
                "-2.45", "MS", "-34.8");
    }
}
