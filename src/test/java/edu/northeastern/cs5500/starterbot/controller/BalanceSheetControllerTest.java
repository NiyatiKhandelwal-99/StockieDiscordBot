package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;

public class BalanceSheetControllerTest {

    private BalanceSheetController getBalanceSheetController() {
        BalanceSheetController balanceSheetController =
                new BalanceSheetController(new FakeBalanceSheetService());
        return balanceSheetController;
    }

    @Test
    public void testValidTickerSymbolForBalanceSheet() throws Exception {
        BalanceSheetController balanceSheetController = getBalanceSheetController();
        final String[] validTickerSymbols = {
            "AAPL", "MSFT", "GOOG", "AMZN", "FB", "TSLA", "NFLX", "NVDA", "PYPL", "ADBE"
        };

        for (String tickerSymbol : validTickerSymbols) {
            assertThat(balanceSheetController.getBalanceSheet(tickerSymbol)).isNotNull();
        }
    }
    @Test
    public void testInvalidTickerSymbolForBalanceSheet() throws Exception {
        BalanceSheetController balanceSheetController = getBalanceSheetController();
        final String[] invalidTickerSymbols = {
            null, ""
        };

        for (String tickerSymbol : invalidTickerSymbols) {
            try {
                System.out.println(tickerSymbol);
                balanceSheetController.getBalanceSheet(tickerSymbol);
                fail("Incorrect Balance Sheet command request failed");
            } catch (BadRequestException e) {
                assertThat(e.getMessage().length() > 0);
            }
        }
    }
}
