package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
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
}
