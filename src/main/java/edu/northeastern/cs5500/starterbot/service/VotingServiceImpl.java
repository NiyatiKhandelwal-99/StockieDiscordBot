package edu.northeastern.cs5500.starterbot.service;

import edu.northeastern.cs5500.starterbot.model.Votes;
import edu.northeastern.cs5500.starterbot.repository.MongoDBRepository;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VotingServiceImpl implements VotingService {

    private final MongoDBRepository mongoDBRepository;

    @Inject
    public VotingServiceImpl(MongoDBService mongoDBService) {
        this.mongoDBRepository = new MongoDBRepository(Votes.class, mongoDBService);
        log.info(Long.toString(mongoDBRepository.count()));
    }

    @Override
    public void register() {
        log.info("VotingServiceImpl > register");
    }

    @Override
    public String upVote(String ticker, String userId) {
        String response = "";
        Collection<Votes> mongoCollection = mongoDBRepository.getAll();
        Boolean doesTickerExist = false;
        for (Votes votes : mongoCollection) {

            if (votes.getTicker().equals(ticker)) {
                doesTickerExist = true;
                log.info("Ticker document " + votes.getTicker() + " exists.");
                ArrayList<String> votersList = votes.getVoters();

                if (votersList.contains(userId)) {
                    response = "You have already voted for " + votes.getTicker() + ".";
                } else {
                    Votes newVote = new Votes();
                    newVote.setId(votes.getId());
                    newVote.setTicker(votes.getTicker());
                    newVote.setVotes(votes.getVotes() + 1);
                    votersList.add(userId);
                    newVote.setVoters(votersList);
                    mongoDBRepository.update(newVote);
                    response =
                            "You have successfully upvoted for ticker " + votes.getTicker() + "!";
                }
                break;
            }
        }

        if (Boolean.FALSE.equals(doesTickerExist)) {
            response = "Ticker does not exist!";
        }
        log.info(response);
        return response;
    }
}
