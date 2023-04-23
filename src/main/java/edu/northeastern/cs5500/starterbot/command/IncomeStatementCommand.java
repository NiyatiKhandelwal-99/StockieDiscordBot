package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotate.ExcludeMethodFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.constants.LogMessages;
import edu.northeastern.cs5500.starterbot.controller.IncomeStatementController;
import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageIncomeStatement;
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
public class IncomeStatementCommand implements SlashCommandHandler {

    public static final int NUMBER_OF_REPORTS = 3;

    @Inject IncomeStatementController incomeStatementController;

    @Inject
    public IncomeStatementCommand() {}

    @Override
    @Nonnull
    public String getName() {
        return "income";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Ask the bot to fetch the income statement")
                .addOption(
                        OptionType.STRING,
                        "ticker",
                        "The bot will return the income statement",
                        true);
    }

    public List<AlphaVantageIncomeStatement> getIncomeStatement(String ticker)
            throws RestException, AlphaVantageException {
        return incomeStatementController.getIncomeStatement(ticker);
    }

    @ExcludeMethodFromGeneratedCoverage
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        log.info("event: /income");
        var option = event.getOption("ticker");

        String ticker = option.getAsString();
        log.info("event: /income ticker:" + ticker);

        List<AlphaVantageIncomeStatement> incomeStatements = null;
        try {
            incomeStatements = getIncomeStatement(ticker);
        } catch (RestException | AlphaVantageException exp) {
            log.error(String.format(LogMessages.ERROR_ALPHAVANTAGE_API, exp.getMessage()), exp);
            event.reply(String.format(LogMessages.ERROR_ALPHAVANTAGE_API_REPLY, ticker)).queue();
            return;
        }

        if (incomeStatements == null) {
            event.reply(String.format(LogMessages.EMPTY_RESPONSE, ticker)).queue();
            return;
        }
        event.reply("Bringing you the Income Sheets for " + ticker.toUpperCase() + "!").queue();
        List<MessageEmbed> incomeStatementEmbeds = renderIncomeStatements(incomeStatements);
        for (MessageEmbed embed : incomeStatementEmbeds) {
            event.getChannel().sendMessageEmbeds(embed).queue();
        }
    }

    public List<MessageEmbed> renderIncomeStatements(
            List<AlphaVantageIncomeStatement> incomeStatements) {
        List<MessageEmbed> messageEmbeds = new ArrayList<>();

        int numberOfReports = incomeStatements.size();
        if (numberOfReports > NUMBER_OF_REPORTS) {
            numberOfReports = NUMBER_OF_REPORTS;
        }
        for (int i = 0; i < numberOfReports; i++) {
            AlphaVantageIncomeStatement alphaVantageIncomeStatement = incomeStatements.get(i);
            messageEmbeds.add(renderIncomeStatement(alphaVantageIncomeStatement));
        }
        return messageEmbeds;
    }

    public MessageEmbed renderIncomeStatement(AlphaVantageIncomeStatement incomeStatement) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Fiscal Date Ending: " + incomeStatement.getFiscalDateEnding());
        embed.addField("Reported Currency", incomeStatement.getReportedCurrency(), true);
        embed.addField("Total Revenue", incomeStatement.getTotalRevenue(), true);
        embed.addField("Cost of Revenue", incomeStatement.getCostOfRevenue(), true);
        embed.addField("Gross Profit", incomeStatement.getGrossProfit(), true);
        embed.addField("Net Income", incomeStatement.getNetIncome(), true);
        embed.addField("Operating Income", incomeStatement.getOperatingIncome(), true);
        embed.addField("Interest Income", incomeStatement.getInterestIncome(), true);
        embed.addField(
                "Research and Development", incomeStatement.getResearchAndDevelopment(), true);
        embed.addField("Income Before Tax", incomeStatement.getIncomeBeforeTax(), true);
        embed.addField("Income Tax Expense", incomeStatement.getIncomeTaxExpense(), true);
        embed.addField("Interest Expense", incomeStatement.getInterestExpense(), true);

        embed.setColor(Color.GREEN);

        return embed.build();
    }
}
