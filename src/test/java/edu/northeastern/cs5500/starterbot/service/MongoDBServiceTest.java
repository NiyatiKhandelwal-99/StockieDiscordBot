package edu.northeastern.cs5500.starterbot.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

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
        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();
        assertNotNull(mongoDatabase);
    }
}