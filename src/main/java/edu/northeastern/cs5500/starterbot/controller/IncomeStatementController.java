package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageIncomeStatement;
import edu.northeastern.cs5500.starterbot.service.IncomeStatementService;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IncomeStatementController {
    IncomeStatementService incomeStatementService;

    @Inject
    IncomeStatementController(IncomeStatementService incomeStatementService) {
        this.incomeStatementService = incomeStatementService;
    }

    public List<AlphaVantageIncomeStatement> getIncomeStatement(String ticker)
            throws RestException, AlphaVantageException {
        if (ticker == null || ticker.length() == 0) {
            throw new BadRequestException("ticker cannot be null or empty");
        }

        ticker = ticker.strip().toUpperCase();

        if (!ticker.matches("^[A-Z]+(?:[.=\\-][A-Z]+)?$")) {
            throw new BadRequestException("ticker had invalid characters");
        }

        return incomeStatementService.getIncomeStatement(ticker);
    }
}
