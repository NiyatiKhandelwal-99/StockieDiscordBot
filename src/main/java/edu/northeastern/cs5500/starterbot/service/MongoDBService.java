package edu.northeastern.cs5500.starterbot.service;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import edu.northeastern.cs5500.starterbot.annotate.ExcludeClassFromGeneratedCoverage;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

@Singleton
@Slf4j
@ExcludeClassFromGeneratedCoverage
public class MongoDBService implements VotingService {

    private static final String DB_NAME = "stock";

    static String getDatabaseURI() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        final String databaseURI = processBuilder.environment().get("MONGODB_URI");
        if (databaseURI != null) {
            return databaseURI;
        }
        throw new IllegalStateException("MONGODB_URI not set; unable to construct MongoDBService!");
    }

    @Getter private MongoDatabase mongoDatabase;

    @Inject
    public MongoDBService() {
        CodecRegistry codecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        ConnectionString connectionString = new ConnectionString(getDatabaseURI());

        MongoClientSettings mongoClientSettings =
                MongoClientSettings.builder()
                        .codecRegistry(codecRegistry)
                        .applyConnectionString(connectionString)
                        .build();

        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
        mongoDatabase = mongoClient.getDatabase(DB_NAME);
    }

    @Override
    public void register() {
        log.info("MongoDBService > register");
    }

    @Override
    public void upVote(
            MongoCollection<org.bson.Document> collection, String ticker, String userId) {
        collection.updateOne(
                Filters.eq("ticker", ticker),
                Updates.combine(Updates.inc("votes", 1), Updates.addToSet("voters", userId)));
    }

    @Override
    public Document findDocument(MongoCollection<Document> collection, String ticker) {
        return collection.find(Filters.eq("ticker", ticker)).first();
    }
}
