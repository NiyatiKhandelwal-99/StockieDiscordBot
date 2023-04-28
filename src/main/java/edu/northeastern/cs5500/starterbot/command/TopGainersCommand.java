package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotate.ExcludeMethodFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.constants.LogMessages;
import edu.northeastern.cs5500.starterbot.controller.TopGainersController;
import edu.northeastern.cs5500.starterbot.exception.YahooFinanceException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

/** TopGainersCommand is responsible for handling the /gainers commands and rendering them on the Discord UI
 * The TopGainersCommand transfers the event details to the controller for further processing.
*/
@Singleton
@Slf4j
public class TopGainersCommand implements SlashCommandHandler {

    private static final int MAX_NUMBER_OF_GAINER_RESULTS = 5;

    @Inject TopGainersController topGainersController;

    @Inject
    public TopGainersCommand() {}

    /**
     * Returns the name of a command
     *
     * @return String : Name of command
     */
    @Nonnull
    @Override
    public String getName() {
        return "gainers";
    }

    /**
     * Returns the structure of the command
     *
     * @return String : Format of the slash command
     */
    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Ask the bot to fetch the top gainers in the market");
    }

    /**
     * getTopGainers function is responsible for calling the controller method for further processiong of the event details
     *
     * @return Map<String, String> : Mapping of tickers and their corresponding price changes
     */
    public Map<String, String> getTopGainers() throws RestException, YahooFinanceException {
        return topGainersController.getTopGainers();
    }

    /**
     * onSlashCommandInteraction is triggered when /gainers command is entered by the user
     *
     * @param event
     */
    @ExcludeMethodFromGeneratedCoverage
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        log.info("event: /gainers ");

        Map<String, String> gainers = new HashMap<>();

        try {
            gainers = getTopGainers();
        } catch (RestException | YahooFinanceException exp) {
            log.error(String.format(LogMessages.ERROR_YAHOOFINANCE_API, exp.getMessage()), exp);
            event.reply(String.format(LogMessages.ERROR_YAHOOFINANCE_API_REPLY)).queue();
            return;
        }

        event.reply("Top Gainers for the day...").queue();

        MessageEmbed messageEmbed = getTopGainersEmbed(gainers);
        event.getChannel().sendMessageEmbeds(messageEmbed).queue();
    }

    /**
     * getTopGainersEmbed function is responsible for creating MessageEmbed suitable for displaying 
     * on discord from the mapping of ticker and price change 
     *
     * @param gainers
     * @return MessageEmbed
     */
    public MessageEmbed getTopGainersEmbed(Map<String, String> gainers) {
        int numberOfGainersEmbedded = 0;
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Ticker" + "     |     " + "Price Change");

        for (Map.Entry<String, String> entry : gainers.entrySet()) {
            numberOfGainersEmbedded++;
            embed.addField("", entry.getKey(), true);
            embed.addField("", "|", true);
            embed.addField("", entry.getValue(), true);
            if (numberOfGainersEmbedded == MAX_NUMBER_OF_GAINER_RESULTS) break;
        }

        embed.setColor(Color.GREEN);
        return embed.build();
    }
}
