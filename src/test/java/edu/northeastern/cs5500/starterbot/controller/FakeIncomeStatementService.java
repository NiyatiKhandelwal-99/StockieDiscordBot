package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageIncomeStatement;
import edu.northeastern.cs5500.starterbot.service.IncomeStatementService;
import java.util.List;

public class FakeIncomeStatementService implements IncomeStatementService {

    public FakeIncomeStatementService() {}

    @Override
    public void register() {
        // Not required to do anything; not doing anything.
    }

    @Override
    public List<AlphaVantageIncomeStatement> getIncomeStatement(String symbol)
            throws RestException, AlphaVantageException {
        return List.of(
                new AlphaVantageIncomeStatement(
                        "2022-12-31",
                        "USD",
                        "60530000000",
                        "27842000000",
                        "32687000000",
                        "1639000000",
                        "6408000000",
                        "6567000000",
                        "162000000",
                        "162000000",
                        "1013000000",
                        "-626000000"),
                new AlphaVantageIncomeStatement(
                        "2021-12-31",
                        "USD",
                        "57350000000",
                        "25865000000",
                        "31486000000",
                        "1639000000",
                        "6408000000",
                        "6488000000",
                        "52000000",
                        "1155000000",
                        "5867000000",
                        "124000000"));
    }
}
