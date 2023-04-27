package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.service.VotingService;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VotingController {

    VotingService votingService;

    @Inject
    VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    public String upVote(String ticker, String userId) throws BadRequestException {
        isTickerValid(ticker);
        return votingService.upVote(ticker, userId);
    }

    public String downVote(String ticker, String userId) throws BadRequestException {
        isTickerValid(ticker);
        return votingService.downVote(ticker, userId);
    }

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
