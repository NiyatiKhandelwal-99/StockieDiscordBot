package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.constants.LogMessages;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageApi;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageGlobalQuote;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public static final String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

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
            log.error(LogMessages.EMPTY_TICKER + event.getName());
            return;
        }

        AlphaVantageGlobalQuote quote = null;
        try {
            quote = alphaVantageApi.getGlobalQuote(option.getAsString());
        } catch (AlphaVantageException e) {
            log.error(LogMessages.ERROR_ALPHAVANTAGE_API_REPLY.toString(), e);
            event.reply(LogMessages.ERROR_ALPHAVANTAGE_API_REPLY.toString()).queue();
            return;
        }
        if (quote == null) {
            event.reply(LogMessages.INVALID_TICKER.toString()).queue();
            return;
        }

        if (quote.getLatestTradingDay().equals(currentDate)) {
            event.reply(
                            "After Market Price: "
                                    + quote.getPrice()
                                    + "\tMarket Price: "
                                    + quote.getPreviousClose())
                    .queue();
            return;
        }
        event.reply("Current price: " + quote.getPrice()).queue();
    }
}
