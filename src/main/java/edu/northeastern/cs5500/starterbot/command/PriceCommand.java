package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageApi;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageGlobalQuote;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Singleton
@Slf4j
public class PriceCommand implements SlashCommandHandler {

    @Inject AlphaVantageApi alphaVantageApi;

    @Inject
    public PriceCommand() {}

    @Nonnull
    @Override
    public String getName() {
        return "price";
    }

    @Nonnull
    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Ask the bot to reply with price")
                .addOption(
                        OptionType.STRING,
                        "ticker",
                        "The bot will reply to your command with the provided text",
                        true);
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /price");
        var option = event.getOption("ticker");

        if (option == null) {
            log.error("Received null value for mandatory parameter 'ticker'");
            return;
        }

        AlphaVantageGlobalQuote quote = null;
        try {
            quote = alphaVantageApi.getGlobalQuote(option.getAsString());
        } catch (AlphaVantageException e) {
            log.error("Error while calling AlphaVantage API", e);
            event.reply("Unexpected error while calling our quote API, please try again later!")
                    .queue();
            return;
        }

        event.reply("Current price: " + quote.getPrice()).queue();
    }
}
