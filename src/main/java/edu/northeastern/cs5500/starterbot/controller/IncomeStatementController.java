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
    public static final int NUMBER_OF_REPORTS = 3;
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

        return limitBalanceSheets(incomeStatementService.getIncomeStatement(ticker));
    }

    public List<AlphaVantageIncomeStatement> limitBalanceSheets(
            List<AlphaVantageIncomeStatement> incomeStatements) {
        int numberOfReports = incomeStatements.size();
        if (numberOfReports <= NUMBER_OF_REPORTS) {
            return incomeStatements;
        } else {
            return incomeStatements.subList(0, NUMBER_OF_REPORTS);
        }
    }
}
