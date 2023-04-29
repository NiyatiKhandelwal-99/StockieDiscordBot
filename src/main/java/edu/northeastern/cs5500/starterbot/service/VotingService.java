package edu.northeastern.cs5500.starterbot.service;

public interface VotingService extends Service {

    /*
     * upVote calls the vote function to perform the UpVote for a particular ticker
     * @param ticker company ticker for which the vote needs to be inserted.
     * @param userId the userId of user who is trying to upVote
     * @return message
     */
    String upVote(String ticker, String userId);

    /*
     * downVote calls the vote function to perform the downVote for a particular ticker
     * @param ticker company ticker for which the vote needs to be inserted
     * @param userId the userId of user who is trying to upVote
     * @return message
     */
    String downVote(String ticker, String userId);
}
