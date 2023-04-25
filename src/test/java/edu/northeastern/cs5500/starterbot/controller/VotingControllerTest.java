package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import org.junit.jupiter.api.Test;

public class VotingControllerTest {

    private VotingController getVotingController() {
        VotingController votingController = new VotingController(new FakeVotingService());
        return votingController;
    }

    @Test
    public void testValidTickerSymbols() throws Exception {
        VotingController votingController = getVotingController();
        final String[] validTickerSymbols = {
            "AAPL", "MSFT", "GOOG", "AMZN", "FB", "TSLA", "NFLX", "NVDA", "PYPL", "ADBE"
        };
        for (String tickerSymbol : validTickerSymbols) {
            try {
                votingController.isTickerValid(tickerSymbol);
            } catch (RestException e) {
                fail(
                        "Expected no RestException to be thrown, but got "
                                + e.getClass().getSimpleName());
            }
        }
    }

    @Test
    public void testInvalidTickerSymbols() throws Exception {
        VotingController votingController = getVotingController();

        try {
            votingController.isTickerValid("#@123");
            fail("Expected RestException to be thrown, but no exception was thrown");
        } catch (RestException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testNullTickerSymbols() throws Exception {
        VotingController votingController = getVotingController();

        try {
            votingController.isTickerValid(null);
            fail("Expected RestException to be thrown, but no exception was thrown");
        } catch (RestException e) {
            assertNotNull(e.getMessage());
        }
    }
}
