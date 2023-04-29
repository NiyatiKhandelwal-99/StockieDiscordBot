package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotate.ExcludeMethodFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.constants.LogMessages;
import edu.northeastern.cs5500.starterbot.controller.WinningPortfoliosController;
import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageWinningPortfolios;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageWinningPortfoliosRankings;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

/**
 * WinningPortfoliosCommand is responsible for handling the /portfolio:date commands and rendering them
 * on the Discord UI The WinningPortfoliosCommand transfers the event details to the controller for
 * further processing.
 */
@Singleton
@Slf4j
public class WinningPortfoliosCommand implements SlashCommandHandler {

    private static final int MAX_NUMBER_OF_PORTFOLIO_RESULTS = 3;

    @Inject WinningPortfoliosController winningPortfoliosController;

    @Inject
    public WinningPortfoliosCommand() {}
    
    /**
     * Returns the name of a command
     *
     * @return String : Name of command
     */
    @Override
    @Nonnull
    public String getName() {
        return "portfolio";
    }

    /**
     * Returns the structure of the command
     *
     * @return String : Format of the slash command
     */
    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(
                        getName(),
                        "Ask the bot to fetch the winning portfolios starting from a specific year and month")
                .addOption(
                        OptionType.STRING,
                        "date",
                        "The bot will return the winning portfolios",
                        true);
    }

    /**
     * getWinningPortfolios function is responsible for calling the controller method for further
     * processiong of the event details
     *
     * @param date : date entered buy the user
     * @return List<AlphaVantageWinningPortfoliosRankings> : The list of winning portfolios to be rendered on UI
     */
    public List<AlphaVantageWinningPortfoliosRankings> getWinningPortfolios(String date)
            throws RestException, AlphaVantageException {
        return winningPortfoliosController.getWinningPortfolios(date);
    }

    /**
     * onSlashCommandInteraction is triggered when /portfolio:date command is entered by the user
     *
     * @param event
     */
    @ExcludeMethodFromGeneratedCoverage
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /portfolio");

        var option = event.getOption("date");
        String date = option.getAsString();

        List<AlphaVantageWinningPortfoliosRankings> winningPortfolios = null;

        try {
            winningPortfolios = getWinningPortfolios(date);
        } catch (RestException | AlphaVantageException exp) {
            log.error(String.format(LogMessages.ERROR_ALPHAVANTAGE_API, exp.getMessage()), exp);
            event.reply(String.format(LogMessages.ERROR_ALPHAVANTAGE_API_REPLY, date)).queue();
            return;
        }

        if (winningPortfolios == null) {
            event.reply(String.format(LogMessages.EMPTY_RESPONSE, date)).queue();
            return;
        }

        event.reply("Winning portfolios from " + date).queue();

        List<MessageEmbed> portfolioEmbeds = renderWinningPortfolios(winningPortfolios);
        for (MessageEmbed embed : portfolioEmbeds) {
            event.getChannel().sendMessageEmbeds(embed).queue();
        }
    }

    /**
     * renderWinningPortfolios function is responsible for creating MessageEmbeds suitable for discord
     * from the AlphaVantageWinningPortfoliosRankings containing information about a winning portfolio.
     *
     * @param List<AlphaVantageWinningPortfoliosRankings>
     * @return List<MessageEmbed>
     */
    public List<MessageEmbed> renderWinningPortfolios(
            List<AlphaVantageWinningPortfoliosRankings> winningPortfolios) {
        List<MessageEmbed> messageEmbeds = new ArrayList<>();
        for (int i = 0;
                i < Math.min(MAX_NUMBER_OF_PORTFOLIO_RESULTS, winningPortfolios.size());
                i++) {
            AlphaVantageWinningPortfoliosRankings alphaVantageWinningPortfoliosRankings =
                    winningPortfolios.get(i);
            messageEmbeds.add(renderWinningPortfolio(alphaVantageWinningPortfoliosRankings));
        }
        return messageEmbeds;
    }

    public MessageEmbed renderWinningPortfolio(
            AlphaVantageWinningPortfoliosRankings alphaVantageWinningPortfoliosRankings) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Rank " + alphaVantageWinningPortfoliosRankings.getRank() + ":");
        embed.setColor(Color.ORANGE);

        embed.addField(
                "Start Date",
                alphaVantageWinningPortfoliosRankings.getMeasurementStartDate(),
                true);
        embed.addField(
                "End Date", alphaVantageWinningPortfoliosRankings.getMeasurementEndDate(), true);
        embed.addBlankField(false);

        embed.addField("Start Value", alphaVantageWinningPortfoliosRankings.getStartValue(), true);
        embed.addField("End Value", alphaVantageWinningPortfoliosRankings.getEndValue(), true);
        embed.addField(
                "Percent Gain", alphaVantageWinningPortfoliosRankings.getPercentGain(), true);

        for (int i = 0; i < alphaVantageWinningPortfoliosRankings.getPortfolios().size(); i++) {
            AlphaVantageWinningPortfolios alphaVantageWinningPortfolios =
                    alphaVantageWinningPortfoliosRankings.getPortfolios().get(i);
            embed.addBlankField(false);
            embed.addField("Symbol", alphaVantageWinningPortfolios.getSymbol(), true);
            embed.addField("Shares", alphaVantageWinningPortfolios.getShares(), true);
        }

        return embed.build();
    }
}
