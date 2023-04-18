package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.BalanceSheetService;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageBalanceSheet;
import java.util.List;

public class FakeBalanceSheetService implements BalanceSheetService {

    public FakeBalanceSheetService() {}

    @Override
    public void register() {
        // Not required to do anything; not doing anything.
    }

    @Override
    public List<AlphaVantageBalanceSheet> getBalanceSheet(String symbol)
            throws RestException, AlphaVantageException {
        return List.of(
                new AlphaVantageBalanceSheet(
                        "352755000000",
                        "2022-09-30",
                        "292870000000",
                        "135405000000",
                        "21239000000",
                        "USD",
                        "302083000000"),
                new AlphaVantageBalanceSheet(
                        "12345", "2021-09-30", "213131", "123144", "21313", "USD", "123123414"));
    }
}
