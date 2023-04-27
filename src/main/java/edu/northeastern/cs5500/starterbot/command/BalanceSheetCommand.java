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

@Singleton
@Slf4j
public class BalanceSheetCommand implements SlashCommandHandler {

    private static final int MAX_NUMBER_OF_BALANCE_SHEET_RESULTS = 3;

    @Inject BalanceSheetController balanceSheetController;

    @Inject
    public BalanceSheetCommand() {}

    @Override
    @Nonnull
    public String getName() {
        return "balance";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Ask the bot to fetch the balance sheet")
                .addOption(
                        OptionType.STRING, "ticker", "The bot will return the balance sheet", true);
    }

    public List<AlphaVantageBalanceSheet> getBalanceSheet(String ticker)
            throws RestException, AlphaVantageException {
        return balanceSheetController.getBalanceSheet(ticker);
    }

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
