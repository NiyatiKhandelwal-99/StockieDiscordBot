package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
public class WinningPortfoliosControllerTest {

    private WinningPortfoliosController getWinningPortfoliosController() {
        WinningPortfoliosController winningPortfoliosController =
                new WinningPortfoliosController(new FakeWinningPortfoliosService());
        return winningPortfoliosController;
    }

    @Test
    public void testValidDateForWinningPortfolios() throws Exception {
        WinningPortfoliosController winningPortfoliosController = getWinningPortfoliosController();
        final String[] validDates = {"2021-11", "2021-10", "2022-04"};

        for (String date : validDates) {
            assertThat(winningPortfoliosController.getWinningPortfolios(date)).isNotEmpty();
        }
    }

    @Test
    public void testInvalidDateForWinningPortfolios() throws Exception {
        WinningPortfoliosController winningPortfoliosController = getWinningPortfoliosController();
        final String[] validDates = {"2021-1211", "2021@#$-10"};

        for (String date : validDates) {
            try {
                winningPortfoliosController.getWinningPortfolios(date);
                fail("Incorrect Winning Portfolio commond request");
            } catch (BadRequestException e) {
                assertThat(e.getMessage().length() > 0);
            }
        }
    }
}
