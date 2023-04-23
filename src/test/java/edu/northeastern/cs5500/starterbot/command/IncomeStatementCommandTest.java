package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.AlphaVantageIncomeStatement;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IncomeStatementCommandTest {

    IncomeStatementCommand incomeStatementCommand;
    CommandData commandData;

    List<AlphaVantageIncomeStatement> incomeStatements;

    @BeforeEach
    void setup() {
        incomeStatementCommand = new IncomeStatementCommand();
        commandData = incomeStatementCommand.getCommandData();
        incomeStatements =
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
                                "124000000"));
    }

    @Test
    void testGetName() {
        String name = new IncomeStatementCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData testIncomeStatementCommandData = new IncomeStatementCommand().getCommandData();
        assertThat(testIncomeStatementCommandData).isNotNull();
        assertThat(testIncomeStatementCommandData.getName()).isEqualTo(commandData.getName());
    }

    @Test
    void testRenderBalanceSheet() {
        IncomeStatementCommand incomeStatementCommand = new IncomeStatementCommand();
        MessageEmbed messageEmbded =
                incomeStatementCommand.renderIncomeStatement(incomeStatements.get(0));

        assertThat(
                messageEmbded
                        .getFields()
                        .get(0)
                        .equals(incomeStatements.get(0).getReportedCurrency()));
        assertThat(
                messageEmbded.getFields().get(1).equals(incomeStatements.get(0).getTotalRevenue()));
        assertThat(
                messageEmbded
                        .getFields()
                        .get(2)
                        .equals(incomeStatements.get(0).getCostOfRevenue()));
        assertThat(
                messageEmbded.getFields().get(3).equals(incomeStatements.get(0).getGrossProfit()));
        assertThat(messageEmbded.getFields().get(4).equals(incomeStatements.get(0).getNetIncome()));
        assertThat(
                messageEmbded
                        .getFields()
                        .get(5)
                        .equals(incomeStatements.get(0).getOperatingIncome()));
        assertThat(
                messageEmbded
                        .getFields()
                        .get(6)
                        .equals(incomeStatements.get(0).getInterestIncome()));
        assertThat(
                messageEmbded
                        .getFields()
                        .get(7)
                        .equals(incomeStatements.get(0).getResearchAndDevelopment()));
        assertThat(
                messageEmbded
                        .getFields()
                        .get(8)
                        .equals(incomeStatements.get(0).getIncomeBeforeTax()));
        assertThat(
                messageEmbded
                        .getFields()
                        .get(9)
                        .equals(incomeStatements.get(0).getIncomeTaxExpense()));

        assertThat(
                messageEmbded
                        .getFields()
                        .get(10)
                        .equals(incomeStatements.get(0).getInterestExpense()));
    }

    @Test
    void testRenderBalanceSheets() {
        IncomeStatementCommand incomeStatementCommand = new IncomeStatementCommand();
        List<MessageEmbed> incomeStatementEmbeds =
                incomeStatementCommand.renderIncomeStatements(incomeStatements);

        assertThat(incomeStatementEmbeds.size()).isEqualTo(2);
    }
}
