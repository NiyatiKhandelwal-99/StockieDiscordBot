package edu.northeastern.cs5500.starterbot.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public interface VotingService extends Service {
    void upVote(MongoCollection<Document> collection, String ticker, String userId);

    MongoDatabase getMongoDatabase();

    Document findDocument(MongoCollection<Document> collection, String ticker);
}
