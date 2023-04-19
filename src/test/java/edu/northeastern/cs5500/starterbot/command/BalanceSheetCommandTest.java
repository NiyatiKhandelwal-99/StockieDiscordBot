package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.AlphaVantageBalanceSheet;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BalanceSheetCommandTest {

    BalanceSheetCommand balanceSheetCommand;
    CommandData commandData;

    List<AlphaVantageBalanceSheet> balanceSheets;

    @BeforeEach
    void setup() {
        balanceSheetCommand = new BalanceSheetCommand();
        commandData = balanceSheetCommand.getCommandData();
        balanceSheets =
                List.of(
                        new AlphaVantageBalanceSheet(
                                "352755000000",
                                "2022-09-30",
                                "292870000000",
                                "135405000000",
                                "21239000000",
                                "USD",
                                "302083000000"),
                        new AlphaVantageBalanceSheet(
                                "12345",
                                "2021-09-30",
                                "213131",
                                "123144",
                                "21313",
                                "USD",
                                "123123414"));
    }

    @Test
    void testGetName() {
        String name = new BalanceSheetCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData testBalanceSheetCommandData = new BalanceSheetCommand().getCommandData();
        assertThat(testBalanceSheetCommandData).isNotNull();
        assertThat(testBalanceSheetCommandData.getName()).isEqualTo(commandData.getName());
    }

    @Test
    void testRenderBalanceSheet() {
        BalanceSheetCommand balanceSheetCommand = new BalanceSheetCommand();
        MessageEmbed messageEmbded = balanceSheetCommand.renderBalanceSheet(balanceSheets.get(0));

        assertThat(
                messageEmbded
                        .getFields()
                        .get(0)
                        .equals(balanceSheets.get(0).getReportedCurrency()));
        assertThat(messageEmbded.getFields().get(1).equals(balanceSheets.get(0).getTotalAssets()));
        assertThat(
                messageEmbded
                        .getFields()
                        .get(2)
                        .equals(balanceSheets.get(0).getTotalCurrentAssets()));
        assertThat(messageEmbded.getFields().get(3).equals(balanceSheets.get(0).getInvestments()));
        assertThat(messageEmbded.getFields().get(4).equals(balanceSheets.get(0).getCurrentDebt()));
        assertThat(
                messageEmbded
                        .getFields()
                        .get(5)
                        .equals(balanceSheets.get(0).getTotalLiabilities()));
    }

    @Test
    void testRenderBalanceSheets() {
        BalanceSheetCommand balanceSheetCommand = new BalanceSheetCommand();
        List<MessageEmbed> balanceSheetEmbeds =
                balanceSheetCommand.renderBalanceSheets(balanceSheets);

        assertThat(balanceSheetEmbeds.size()).isEqualTo(2);
    }
}
