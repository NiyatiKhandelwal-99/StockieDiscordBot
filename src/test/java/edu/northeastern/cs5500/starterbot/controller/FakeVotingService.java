package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.northeastern.cs5500.starterbot.service.VotingService;
import org.bson.Document;

public class FakeVotingService implements VotingService {

    public FakeVotingService() {}

    @Override
    public void register() {
        // Not required to do anything; not doing anything.
    }

    @Override
    public void upVote(MongoCollection<Document> collection, String ticker, String userId) {
        // Not required to do anything; not doing anything.
    }

    @Override
    public MongoDatabase getMongoDatabase() {
        /// Not required to do anything; not doing anything.
        return null;
    }

    @Override
    public Document findDocument(MongoCollection<Document> collection, String ticker) {
        // Not required to do anything; not doing anything.
        return null;
    }
}
