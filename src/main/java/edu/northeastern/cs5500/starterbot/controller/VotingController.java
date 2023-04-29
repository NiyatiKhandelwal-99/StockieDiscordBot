package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.service.VotingService;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * VotingController is responsible for processing and verifying the event details passed by the user
 * and calling the service to perform the business logic.
 */
@Singleton
public class VotingController {

    VotingService votingService;

    @Inject
    VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    /**
     * upVote verifies the ticker validity and calls the service to perform the logic
     *
     * @param ticker
     * @param userId
     * @return String message
     * @throws BadRequestException
     */
    public String upVote(String ticker, String userId) throws BadRequestException {
        isTickerValid(ticker);
        return votingService.upVote(ticker, userId);
    }

    /**
     * downVote verifies the ticker validity and calls the service to perform the logic
     *
     * @param ticker
     * @param userId
     * @return String message
     * @throws BadRequestException
     */
    public String downVote(String ticker, String userId) throws BadRequestException {
        isTickerValid(ticker);
        return votingService.downVote(ticker, userId);
    }

    /*
     * isTickerValid verifies if the ticker is valid
     *
     * @param ticker
     * @throws BadRequestException
     */
    public void isTickerValid(String ticker) throws BadRequestException {
        if (ticker == null || ticker.length() == 0) {
            throw new BadRequestException("ticker cannot be null or empty");
        }
        ticker = ticker.strip().toUpperCase();

        if (!ticker.matches("^[A-Z]+(?:[.=\\-][A-Z]+)?$")) {
            throw new BadRequestException("ticker had invalid characters");
        }
    }
}
