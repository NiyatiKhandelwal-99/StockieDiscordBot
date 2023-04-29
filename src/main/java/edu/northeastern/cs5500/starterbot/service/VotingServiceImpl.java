package edu.northeastern.cs5500.starterbot.service;

import edu.northeastern.cs5500.starterbot.annotate.ExcludeClassFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.model.Votes;
import edu.northeastern.cs5500.starterbot.repository.MongoDBRepository;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/*
 * VotingServiceImpl implements Voting Service and consists of the business logic to perform operations related to voting.
 */
@Slf4j
@ExcludeClassFromGeneratedCoverage
public class VotingServiceImpl implements VotingService {

    private final MongoDBRepository mongoDBRepository;

    /*
     * VotingServiceImpl serves as a constructor that initialises the mongoDBRepository
     * @param mongoDBService MongoDB Service
     */
    @Inject
    public VotingServiceImpl(MongoDBService mongoDBService) {
        this.mongoDBRepository = new MongoDBRepository(Votes.class, mongoDBService);
    }

    @Override
    public void register() {
        log.info("VotingServiceImpl > register");
    }

    /*
     * upVote calls the vote function to perform the UpVote for a particular ticker
     * @param ticker company ticker for which the vote needs to be inserted.
     * @param userId the userId of user who is trying to upVote
     * @return message
     */
    @Override
    public String upVote(String ticker, String userId) {
        return vote(ticker, userId, 1);
    }

    /*
     * downVote calls the vote function to perform the downVote for a particular ticker
     * @param ticker company ticker for which the vote needs to be inserted
     * @param userId the userId of user who is trying to upVote
     * @return message
     */
    @Override
    public String downVote(String ticker, String userId) {
        return vote(ticker, userId, -1);
    }

    /*
     * vote function performs the actual voting for a ticker
     * @param ticker company ticker for which the vote needs to be inserted/deleted
     * @param userId the userId of user who is trying to downVote
     * @param vote +1 for upVote and -1 for downVote
     * @return message (response)
     */
    public String vote(String ticker, String userId, int vote) {
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
                    votersList.add(userId);
                    Votes newVote =
                            new Votes(
                                    votes.getId(),
                                    votes.getTicker(),
                                    votes.getVotes() + vote,
                                    votersList);
                    mongoDBRepository.update(newVote);
                    response = "You have successfully voted for ticker " + votes.getTicker() + "!";
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
