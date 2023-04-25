package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.service.VotingService;

public class FakeVotingService implements VotingService {

    public FakeVotingService() {}

    @Override
    public void register() {
    }

    @Override
    public String upVote(String ticker, String userId) {
        return "test";
    }
}
