package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.client.MongoCollection;
import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.VotingService;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bson.Document;

@Singleton
public class VotingController {

    VotingService votingService;

    @Inject
    VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    public void upVote(String ticker, String userId) {
        votingService.upVote(ticker, userId);
    }

    public Document findDocument(MongoCollection<Document> collection, String ticker)
            throws RestException {

        isTickerValid(ticker);

        return votingService.findDocument(collection, ticker);
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
