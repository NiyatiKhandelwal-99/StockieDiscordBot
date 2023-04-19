package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageBalanceSheet;
import edu.northeastern.cs5500.starterbot.service.BalanceSheetService;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BalanceSheetController {

    BalanceSheetService balanceSheetService;

    @Inject
    BalanceSheetController(BalanceSheetService balanceSheetService) {
        this.balanceSheetService = balanceSheetService;
    }

    public List<AlphaVantageBalanceSheet> getBalanceSheet(String ticker)
            throws RestException, AlphaVantageException {
        if (ticker == null || ticker.length() == 0) {
            throw new BadRequestException("ticker cannot be null or empty");
        }

        ticker = ticker.strip().toUpperCase();

        if (!ticker.matches("^[A-Z]+(?:[.=\\-][A-Z]+)?$")) {
            throw new BadRequestException("ticker had invalid characters");
        }
        return balanceSheetService.getBalanceSheet(ticker);
    }
}
