package edu.northeastern.cs5500.starterbot.service;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

@Singleton
@Slf4j
public class MongoDBService implements Service {

    private static final String DEFAULT_DB_NAME = "stock";
    private static final String DEFAULT_DB_URI = "mongodb+srv://stock_username:stock_password@stockie.ujsgrdy.mongodb.net/?retryWrites=true";

    static String getDatabaseURI() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        final String databaseURI = processBuilder.environment().get("MONGODB_URI");
        if (databaseURI != null) {
            return databaseURI;
        }
        return DEFAULT_DB_URI; // connect to localhost by default
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

        mongoDatabase = mongoClient.getDatabase(DEFAULT_DB_NAME);
    }

    @Override
    public void register() {
        log.info("MongoDBService > register");
    }
}
