package edu.northeastern.cs5500.starterbot.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import edu.northeastern.cs5500.starterbot.model.Votes;
import edu.northeastern.cs5500.starterbot.repository.MongoDBRepository;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

@Slf4j
public class VotingServiceImpl implements VotingService {

    private final MongoDBRepository mongoDBRepository;
    private final MongoDBService mongoDBService;

    @Inject
    public VotingServiceImpl(MongoDBService mongoDBService) {
        // this.mongoDBRepository = mongoDBRepository;
        this.mongoDBService = mongoDBService;
        this.mongoDBRepository = new MongoDBRepository(Votes.class, mongoDBService);
        log.info(Long.toString(mongoDBRepository.count()));
    }

    @Override
    public void register() {
        log.info("VotingServiceImpl > register");
    }

    @Override
    public void upVote(String ticker, String userId) {

        mongoDBRepository.update(
                Filters.eq("ticker", ticker),
                Updates.combine(Updates.inc("votes", 1), Updates.addToSet("voters", userId)));
    }

    @Override
    public Document findDocument(MongoCollection<Document> collection, String ticker) {
        return collection.find(Filters.eq("ticker", ticker)).first();
    }
}
