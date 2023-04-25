package edu.northeastern.cs5500.starterbot.service;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public interface VotingService extends Service {
    void upVote(String ticker, String userId);

    Document findDocument(MongoCollection<Document> collection, String ticker);
}
