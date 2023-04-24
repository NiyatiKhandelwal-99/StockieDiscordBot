package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
public class IncomeStatementControllerTest {

    private IncomeStatementController getIncomeStatementController() {
        IncomeStatementController incomeStatementController =
                new IncomeStatementController(new FakeIncomeStatementService());
        return incomeStatementController;
    }

    @Test
    public void testValidTickerSymbolForIncomeStatement() throws Exception {
        IncomeStatementController incomeStatementController = getIncomeStatementController();
        final String[] validTickerSymbols = {
            "AAPL", "MSFT", "GOOG", "AMZN", "FB", "TSLA", "NFLX", "NVDA", "PYPL", "ADBE"
        };

        for (String tickerSymbol : validTickerSymbols) {
            assertThat(incomeStatementController.getIncomeStatement(tickerSymbol)).isNotNull();
        }
    }

    @Test
    public void testInvalidTickerSymbolForIncomeStatement() throws Exception {
        IncomeStatementController incomeStatementController = getIncomeStatementController();
        final String[] invalidTickerSymbols = {null, "", "$%^&ewqar"};

        for (String tickerSymbol : invalidTickerSymbols) {
            try {
                incomeStatementController.getIncomeStatement(tickerSymbol);
                fail("Incorrect Income Statement command request failed");
            } catch (BadRequestException e) {
                assertThat(e.getMessage().length() > 0);
            }
        }
    }
}
