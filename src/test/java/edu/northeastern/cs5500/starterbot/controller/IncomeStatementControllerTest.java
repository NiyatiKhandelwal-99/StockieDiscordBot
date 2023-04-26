package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageIncomeStatement;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "ALPHA_VANTAGE_API_KEY", matches = ".+")
public class IncomeStatementControllerTest {

    public static final int NUMBER_OF_REPORTS = 3;

    private IncomeStatementController getIncomeStatementController() {
        IncomeStatementController incomeStatementController =
                new IncomeStatementController(new FakeIncomeStatementService());
        return incomeStatementController;
    }

    @Test
    public void testValidTickerSymbolForIncomeStatement() throws Exception {
        IncomeStatementController incomeStatementController = getIncomeStatementController();
        final String[] validTickerSymbols = {
            "AAPL", "MSFT", "GOOG", "AMZN", "TSLA", "NFLX", "NVDA", "PYPL", "ADBE"
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

    @Test
    public void testLimitBalanceSheets() {
        List<AlphaVantageIncomeStatement> incomeStatements =
                List.of(
                        new AlphaVantageIncomeStatement(
                                "2022-12-31",
                                "USD",
                                "60530000000",
                                "27842000000",
                                "32687000000",
                                "1639000000",
                                "6408000000",
                                "6567000000",
                                "162000000",
                                "162000000",
                                "1013000000",
                                "-626000000"),
                        new AlphaVantageIncomeStatement(
                                "2021-12-31",
                                "USD",
                                "57350000000",
                                "25865000000",
                                "31486000000",
                                "1639000000",
                                "6408000000",
                                "6488000000",
                                "52000000",
                                "1155000000",
                                "5867000000",
                                "124000000"),
                        new AlphaVantageIncomeStatement(
                                "2022-12-31",
                                "USD",
                                "60530000000",
                                "27842000000",
                                "32687000000",
                                "1639000000",
                                "6408000000",
                                "6567000000",
                                "162000000",
                                "162000000",
                                "1013000000",
                                "-626000000"),
                        new AlphaVantageIncomeStatement(
                                "2022-12-31",
                                "USD",
                                "60530000000",
                                "27842000000",
                                "32687000000",
                                "1639000000",
                                "6408000000",
                                "6567000000",
                                "162000000",
                                "162000000",
                                "1013000000",
                                "-626000000"));
        IncomeStatementController incomeStatementController = getIncomeStatementController();
        int size = incomeStatementController.limitBalanceSheets(incomeStatements).size();
        assertThat(size).isEqualTo(NUMBER_OF_REPORTS);
    }

    @Test
    public void testLimitBalanceSheetsWithLessIncomeStatements() {
        List<AlphaVantageIncomeStatement> incomeStatements =
                List.of(
                        new AlphaVantageIncomeStatement(
                                "2022-12-31",
                                "USD",
                                "60530000000",
                                "27842000000",
                                "32687000000",
                                "1639000000",
                                "6408000000",
                                "6567000000",
                                "162000000",
                                "162000000",
                                "1013000000",
                                "-626000000"));
        IncomeStatementController incomeStatementController = getIncomeStatementController();
        int size = incomeStatementController.limitBalanceSheets(incomeStatements).size();
        assertThat(size).isEqualTo(incomeStatements.size());
    }

    @Test
    public void testLimitBalanceSheetsWithNullIncomeStatements() {
        IncomeStatementController incomeStatementController = getIncomeStatementController();
        assertThat(incomeStatementController.limitBalanceSheets(null)).isEqualTo(null);
    }
}
