package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.AlphaVantageWinningPortfolios;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageWinningPortfoliosRankings;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WinningPortfoliosCommandTest {

    WinningPortfoliosCommand winningPortfoliosCommand;
    CommandData commandData;

    List<AlphaVantageWinningPortfoliosRankings> winningPortfolios;

    @BeforeEach
    void setup() {
        winningPortfoliosCommand = new WinningPortfoliosCommand();
        commandData = winningPortfoliosCommand.getCommandData();

        winningPortfolios =
                List.of(
                        new AlphaVantageWinningPortfoliosRankings(
                                "1",
                                List.of(
                                        new AlphaVantageWinningPortfolios("AAPL", "100"),
                                        new AlphaVantageWinningPortfolios("GOOG", "10")),
                                "2021-11-12",
                                "2021-11-26",
                                "28.48723",
                                "2036.0",
                                "2616.0"),
                        new AlphaVantageWinningPortfoliosRankings(
                                "2",
                                List.of(
                                        new AlphaVantageWinningPortfolios("MSFT", "5"),
                                        new AlphaVantageWinningPortfolios("TSLA", "1000")),
                                "2021-11-12",
                                "2021-11-26",
                                "20.92809",
                                "105313.0",
                                "127353.0"));
    }

    @Test
    void testGetCommandData() {
        CommandData testWinningPortfoliosCommand = new WinningPortfoliosCommand().getCommandData();
        assertThat(testWinningPortfoliosCommand).isNotNull();
        assertThat(testWinningPortfoliosCommand.getName()).isEqualTo(commandData.getName());
    }

    @Test
    void testGetName() {
        String name = new WinningPortfoliosCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testRenderWinningPortfolio() {
        WinningPortfoliosCommand winningPortfoliosCommand = new WinningPortfoliosCommand();
        MessageEmbed messageEmbed =
                winningPortfoliosCommand.renderWinningPortfolio(winningPortfolios.get(0));

        assertThat(messageEmbed.getFields().get(0).getValue())
                .isEqualTo(winningPortfolios.get(0).getMeasurementStartDate());

        assertThat(messageEmbed.getFields().get(1).getValue())
                .isEqualTo(winningPortfolios.get(0).getMeasurementEndDate());
        ;
        assertThat(messageEmbed.getFields().get(3).getValue())
                .isEqualTo(winningPortfolios.get(0).getStartValue());
        assertThat(messageEmbed.getFields().get(4).getValue())
                .isEqualTo(winningPortfolios.get(0).getEndValue());
        assertThat(messageEmbed.getFields().get(5).getValue())
                .isEqualTo(winningPortfolios.get(0).getPercentGain());
        ;

        assertThat(messageEmbed.getFields().get(7).getValue())
                .isEqualTo(winningPortfolios.get(0).getPortfolios().get(0).getSymbol());

        assertThat(messageEmbed.getFields().get(8).getValue())
                .isEqualTo(winningPortfolios.get(0).getPortfolios().get(0).getShares());
    }

    @Test
    void testRenderWinningPortfolios() {
        WinningPortfoliosCommand winningPortfoliosCommand = new WinningPortfoliosCommand();
        List<MessageEmbed> messageEmbeds =
                winningPortfoliosCommand.renderWinningPortfolios(winningPortfolios);
        assertThat(messageEmbeds.size()).isEqualTo(2);
    }
}
