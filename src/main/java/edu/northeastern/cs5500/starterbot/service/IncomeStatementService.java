package edu.northeastern.cs5500.starterbot.service;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageIncomeStatement;
import java.util.List;

public interface IncomeStatementService extends Service {
    List<AlphaVantageIncomeStatement> getIncomeStatement(String symbol)
            throws RestException, AlphaVantageException;
}
