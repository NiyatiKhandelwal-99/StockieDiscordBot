package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.QuoteController;
import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.MissingRequiredParameterException;
import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.exception.rest.NotFoundException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageGlobalQuote;
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
    @Inject QuoteController quoteController;

    @Inject
    public PriceCommand() {
        // empty constructor required for injection
    }

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
            throw new MissingRequiredParameterException("ticker");
        }

        var tickerSymbol = option.getAsString();

        final AlphaVantageGlobalQuote quote;

        try {
            quote = quoteController.getQuote(tickerSymbol);
        } catch (AlphaVantageException ave) {
            event.reply("Invalid ticker symbol").queue();
            return;
        } catch (BadRequestException bre) {
            event.reply("Invalid ticker symbol").queue();
            return;
        } catch (NotFoundException nfe) {
            event.reply("Ticker symbol not found").queue();
            return;
        } catch (RestException e) {
            log.error("Error getting quote", e);
            event.reply("Error getting quote").queue();
            return;
        }

        final String message = formatMessage(quote);

        event.reply(message).queue();
    }

    @Nonnull
    public static String formatMessage(AlphaVantageGlobalQuote quote) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        final String message;
        if (currentDate.equals(quote.getLatestTradingDay())) {
            message =
                    "After Market Price: "
                            + quote.getPrice()
                            + "\tMarket Price: "
                            + quote.getPreviousClose();
        } else {
            message = "Current price: " + quote.getPrice();
        }

        return message;
    }
}
