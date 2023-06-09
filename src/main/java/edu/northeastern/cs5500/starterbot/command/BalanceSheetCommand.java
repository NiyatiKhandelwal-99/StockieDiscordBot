package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotate.ExcludeMethodFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.constants.LogMessages;
import edu.northeastern.cs5500.starterbot.controller.BalanceSheetController;
import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageBalanceSheet;
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
 * BalanceSheetCommand is responsible for handling the /balance:ticker commands and rendering them
 * on the Discord UI The BalanceSheetCommand transfers the event details to the controller for
 * further processing.
 */
@Singleton
@Slf4j
public class BalanceSheetCommand implements SlashCommandHandler {

    private static final int MAX_NUMBER_OF_BALANCE_SHEET_RESULTS = 3;

    @Inject BalanceSheetController balanceSheetController;

    @Inject
    public BalanceSheetCommand() {
        /* This constructor is required to facilitate injection. */
    }

    /**
     * Returns the name of a command
     *
     * @return String : Name of command
     */
    @Override
    @Nonnull
    public String getName() {
        return "balance";
    }

    /**
     * Returns the structure of the command
     *
     * @return String : Format of the slash command
     */
    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Ask the bot to fetch the balance sheet")
                .addOption(
                        OptionType.STRING, "ticker", "The bot will return the balance sheet", true);
    }

    /**
     * GetBalanceSheet function is responsible for calling the controller method for further
     * processiong of the event details
     *
     * @param ticker : ticker symbol entered buy the user
     * @return List<AlphaVantageBalanceSheet> : The list of balance sheets to be rendered on UI
     */
    public List<AlphaVantageBalanceSheet> getBalanceSheet(String ticker)
            throws RestException, AlphaVantageException {
        return balanceSheetController.getBalanceSheet(ticker);
    }

    /**
     * onSlashCommandInteraction is triggered when /balance command is entered by the user
     *
     * @param event
     */
    @ExcludeMethodFromGeneratedCoverage
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /balance");

        var option = event.getOption("ticker");
        String ticker = option.getAsString();

        log.info("Ticker from balanceSheetCommand.java is: " + ticker);

        List<AlphaVantageBalanceSheet> balanceSheets = null;
        try {
            balanceSheets = getBalanceSheet(ticker);
        } catch (RestException | AlphaVantageException exp) {
            log.error(String.format(LogMessages.ERROR_ALPHAVANTAGE_API, exp.getMessage()), exp);
            event.reply(String.format(LogMessages.ERROR_ALPHAVANTAGE_API_REPLY, ticker)).queue();
            return;
        }

        if (balanceSheets == null) {
            event.reply(String.format(LogMessages.EMPTY_RESPONSE, ticker)).queue();
            return;
        }

        event.reply("Latest balance sheets for " + ticker).queue();

        List<MessageEmbed> balanceSheetEmbeds = renderBalanceSheets(balanceSheets);
        for (MessageEmbed embed : balanceSheetEmbeds) {
            event.getChannel().sendMessageEmbeds(embed).queue();
        }
    }

    /**
     * renderBalanceSheets function is responsible for creating MessageEmbeds suitable for discord
     * from the AlphaVantageBalanceSheets containing information about a ticker's balance sheet.
     *
     * @param balanceSheets
     * @return List<MessageEmbed>
     */
    public List<MessageEmbed> renderBalanceSheets(List<AlphaVantageBalanceSheet> balanceSheets) {
        List<MessageEmbed> messageEmbeds = new ArrayList<>();
        for (int i = 0;
                i < Math.min(MAX_NUMBER_OF_BALANCE_SHEET_RESULTS, balanceSheets.size());
                i++) {
            AlphaVantageBalanceSheet alphaVantageBalanceSheet = balanceSheets.get(i);
            messageEmbeds.add(renderBalanceSheet(alphaVantageBalanceSheet));
        }
        return messageEmbeds;
    }

    /**
     * renderBalanceSheet function is responsible for creating a single embed from a single
     * AlphaVantageBalanceSheet object and setting the title, necessary fields required.
     *
     * @param balanceSheet
     * @return MessageEmbed
     */
    public MessageEmbed renderBalanceSheet(AlphaVantageBalanceSheet balanceSheet) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Fiscal Date Ending: " + balanceSheet.getFiscalDateEnding());
        embed.addField("Reported Currency", balanceSheet.getReportedCurrency(), true);
        embed.addField("Total Assets", balanceSheet.getTotalAssets(), true);
        embed.addField("Total Current Assets", balanceSheet.getTotalCurrentAssets(), false);
        embed.addField("Investments", balanceSheet.getInvestments(), true);
        embed.addField("Current Debt", balanceSheet.getCurrentDebt(), true);
        embed.addField("Total Liabilities", balanceSheet.getTotalLiabilities(), false);

        embed.setColor(Color.GREEN);

        return embed.build();
    }
}
