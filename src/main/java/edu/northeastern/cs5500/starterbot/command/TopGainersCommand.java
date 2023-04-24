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

@Singleton
@Slf4j
public class TopGainersCommand implements SlashCommandHandler {

    private static final int MAX_NUMBER_OF_GAINER_RESULTS = 5;

    @Inject TopGainersController topGainersController;

    @Inject
    public TopGainersCommand() {}

    @Nonnull
    @Override
    public String getName() {
        return "gainers";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Ask the bot to fetch the top gainers in the market");
    }

    public Map<String, String> getTopGainers() throws RestException, YahooFinanceException {
        return topGainersController.getTopGainers();
    }

    @ExcludeMethodFromGeneratedCoverage
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        log.info("event: /gainers ");

        Map<String, String> gainers = new HashMap<>();

        try {
            gainers = getTopGainers();
        } catch (RestException | YahooFinanceException exp) {
            log.error(String.format(LogMessages.ERROR_ALPHAVANTAGE_API, exp.getMessage()), exp);
            event.reply(String.format(LogMessages.ERROR_ALPHAVANTAGE_API_REPLY)).queue();
            return;
        }

        event.reply("Top Gainers for the day...").queue();

        MessageEmbed messageEmbed = getTopGainersEmbed(gainers);
        event.getChannel().sendMessageEmbeds(messageEmbed).queue();
    }

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
