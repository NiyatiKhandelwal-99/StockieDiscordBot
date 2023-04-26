package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import edu.northeastern.cs5500.starterbot.exception.YahooFinanceException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
public class TopLosersControllerTest {

    private TopLosersController getTopLosersController() {
        TopLosersController topLosersController =
                new TopLosersController(new FakeTopLosersService());
        return topLosersController;
    }

    @Test
    void testGetTopLosers() {
        TopLosersController topLosersController = getTopLosersController();
        try {
            assertThat(topLosersController.getTopLosers()).isNotNull();
        } catch (YahooFinanceException | RestException e) {
            fail("Expected to succeed by returning a map of ticker symbol and price changes");
        }
    }
}
