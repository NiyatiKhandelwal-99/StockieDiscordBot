package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageWinningPortfoliosRankings;
import edu.northeastern.cs5500.starterbot.service.WinningPortfoliosService;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * WinningPortfoliosController is responsible for processing and verifying the event details passed by
 * the user and calling the service to perform the business logic.
 */
@Singleton
public class WinningPortfoliosController {

    WinningPortfoliosService winningPortfoliosService;

    @Inject
    WinningPortfoliosController(WinningPortfoliosService winningPortfoliosService) {
        this.winningPortfoliosService = winningPortfoliosService;
    }

    /**
     * getWinningPortfolios verifies the date validity and calls the service to perform the logic
     *
     * @param date
     * @return List<AlphaVantageWinningPortfoliosRankings>
     * @throws RestException
     * @throws AlphaVantageException
     */
    public List<AlphaVantageWinningPortfoliosRankings> getWinningPortfolios(String date)
            throws RestException, AlphaVantageException {

        if (date == null || date.length() == 0) {
            throw new BadRequestException("ticker cannot be null or empty");
        }

        String datePattern = "^\\d{4}-(0[1-9]|1[0-2])$";
        if (!date.matches(datePattern)) {
            throw new BadRequestException("ticker had invalid characters");
        }

        return winningPortfoliosService.getWinningPortfolios(date);
    }
}
