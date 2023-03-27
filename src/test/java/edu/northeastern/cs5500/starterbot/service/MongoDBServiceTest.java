package edu.northeastern.cs5500.starterbot.service;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class MongoDBServiceTest {

    private MongoDBService getMongoDBService() {
        return new MongoDBService();
    }

    @Test
    @Order(1)
    void testGetMongoDBService() {
        assertNotNull(getMongoDBService());
    }

    @Test
    @Order(2)
    void testMongoDBCollections() {
        MongoDBService mongoDBService = getMongoDBService();
        MongoClient mongoClient = mongoDBService.getMongoClient();
        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();

        assertNotNull(mongoClient);
        assertNotNull(mongoDatabase);
    }

    @Test
    @Order(3)
    void testMongoDBCollectionInsertion() {
        MongoDBService mongoDBService = getMongoDBService();
        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();

        MongoCollection<Document> collection = mongoDatabase.getCollection("stockie_test");
        Document document = new Document("field_name", "field_value").append("_id", "primary_key");

        collection.insertOne(document);
    }

    @Test
    @Order(4)
    void testMongoDBCollectionUpdate() {
        MongoDBService mongoDBService = getMongoDBService();
        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();

        MongoCollection<Document> collection = mongoDatabase.getCollection("stockie_test");

        UpdateResult updateResult =
                collection.updateOne(
                        eq("_id", "primary_key"), set("field_name", "updated_field_value"));

        assertTrue(updateResult.getModifiedCount() > 0);
        assertEquals(1, collection.countDocuments());
        long count = collection.countDocuments(eq("_id", "primary_key"));
        assertEquals(1, count);
    }

    @Test
    @Order(5)
    void testMongoDBCollectionDelete() {
        MongoDBService mongoDBService = getMongoDBService();
        MongoClient mongoClient = mongoDBService.getMongoClient();
        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();

        MongoCollection<Document> collection = mongoDatabase.getCollection("stockie_test");

        System.out.println("Total Documents: " + collection.countDocuments());

        long count = collection.countDocuments(eq("_id", "primary_key"));
        assertEquals(1, count);

        collection.findOneAndDelete(eq("_id", "primary_key"));

        count = collection.countDocuments(eq("_id", "primary_key"));
        assertEquals(0, count);

        mongoClient.close();
    }
}
