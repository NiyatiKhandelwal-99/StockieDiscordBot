package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import edu.northeastern.cs5500.starterbot.exception.YahooFinanceException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
public class TopGainersControllerTest {

    private TopGainersController getTopGainersController() {
        TopGainersController topGainersController =
                new TopGainersController(new FakeTopGainersService());
        return topGainersController;
    }

    @Test
    void testGetTopGainers() {
        TopGainersController topGainersController = getTopGainersController();
        try {
            assertThat(topGainersController.getTopGainers()).isNotNull();
        } catch (YahooFinanceException | RestException e) {
            fail("Expected to succeed by returning a map of ticker symbol and price changes");
        }
    }
}
