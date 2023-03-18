package edu.northeastern.cs5500.starterbot.service;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MongoDBServiceTest {

    private MongoDBService getMongoDBService() {
        return new MongoDBService();
    }

    @Test
    void testGetMongoDBService() {
        assertNotNull(getMongoDBService());
    }
}