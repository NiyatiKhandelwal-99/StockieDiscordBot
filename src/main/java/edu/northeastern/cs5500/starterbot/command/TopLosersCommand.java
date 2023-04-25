package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotate.ExcludeMethodFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.constants.LogMessages;
import edu.northeastern.cs5500.starterbot.controller.TopLosersController;
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

@Slf4j
@Singleton
public class TopLosersCommand implements SlashCommandHandler {

    private static final int MAX_NUMBER_OF_LOSER_RESULTS = 5;

    @Inject TopLosersController topLosersController;

    @Inject
    public TopLosersCommand() {}

    @Override
    @Nonnull
    public String getName() {
        return "losers";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Ask the bot to fetch the top losers in the market");
    }

    @ExcludeMethodFromGeneratedCoverage
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /gainers ");

        Map<String, String> losers = new HashMap<>();

        try {
            losers = getTopLosers();
        } catch (RestException | YahooFinanceException exp) {
            log.error(String.format(LogMessages.ERROR_YAHOOFINANCE_API, exp.getMessage()), exp);
            event.reply(String.format(LogMessages.ERROR_YAHOOFINANCE_API_REPLY)).queue();
            return;
        }

        event.reply("Top Losers for the day...").queue();

        MessageEmbed messageEmbed = getTopLosersEmbed(losers);
        event.getChannel().sendMessageEmbeds(messageEmbed).queue();
    }

    public MessageEmbed getTopLosersEmbed(Map<String, String> losers) {
        int numberOfLosersEmbedded = 0;
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Ticker" + "     |     " + "Price Change");

        for (Map.Entry<String, String> entry : losers.entrySet()) {
            numberOfLosersEmbedded++;
            embed.addField("", entry.getKey(), true);
            embed.addField("", "|", true);
            embed.addField("", entry.getValue(), true);
            if (numberOfLosersEmbedded == MAX_NUMBER_OF_LOSER_RESULTS) break;
        }

        embed.setColor(Color.RED);
        return embed.build();
    }

    public Map<String, String> getTopLosers() throws RestException, YahooFinanceException {
        return topLosersController.getTopLosers();
    }
}
