package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

    public MongoDatabase getMongoDatabase() {
        return votingService.getMongoDatabase();
    }

    public void upVote(MongoCollection<Document> collection, String ticker, String userId) {
        votingService.upVote(collection, ticker, userId);
    }
}
