package edu.northeastern.cs5500.starterbot.service;

public interface VotingService extends Service {
    String upVote(String ticker, String userId);

    String downVote(String ticker, String userId);
}
