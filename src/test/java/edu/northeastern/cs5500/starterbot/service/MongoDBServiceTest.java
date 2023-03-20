package edu.northeastern.cs5500.starterbot.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

class MongoDBServiceTest {

    private MongoDBService getMongoDBService() {
        return new MongoDBService();
    }

    @Test
    void testGetMongoDBService() {
        assertNotNull(getMongoDBService());
    }

    @Test 
    void testMongoDBCollections() {
        MongoDBService mongoDBService = getMongoDBService();
        MongoClient mongoClient = mongoDBService.getMongoClient();
        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();

        assertNotNull(mongoClient);
        assertNotNull(mongoDatabase);

        mongoClient.close();
    }
}