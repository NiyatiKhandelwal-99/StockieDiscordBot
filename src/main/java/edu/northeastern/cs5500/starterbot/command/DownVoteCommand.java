package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotate.ExcludeMethodFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.constants.LogMessages;
import edu.northeastern.cs5500.starterbot.controller.VotingController;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Singleton
@Slf4j
public class DownVoteCommand implements SlashCommandHandler {

    public static final String VOTES = "votes";
    public static final String VOTERS = "voters";
    public static final String TICKER = "ticker";

    @Inject VotingController votingController;

    @Inject
    public DownVoteCommand() {}

    @Nonnull
    @Override
    public String getName() {
        return "downvote";
    }

    @Nonnull
    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Ask the bot to downvote for a particular ticker")
                .addOption(
                        OptionType.STRING,
                        TICKER,
                        "The bot will downvote the provided ticker",
                        true);
    }

    @ExcludeMethodFromGeneratedCoverage
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        log.info("event: /downvote");
        var option = event.getOption(TICKER);

        String userId = null;

        if (event.getMember() != null) {
            Member member = event.getMember();
            if (member != null) userId = member.getId();
        }

        if (option == null) {
            log.error(LogMessages.EMPTY_TICKER, event.getName());
            return;
        }

        String ticker = option.getAsString().toLowerCase();

        log.info("event: /downvote ticker:" + ticker);

        String voteResult;
        try {
            voteResult = votingController.downVote(ticker, userId);
        } catch (RestException e) {
            log.error(String.format(LogMessages.INVALID_TICKER, e.getMessage()), e);
            event.reply(String.format(LogMessages.INVALID_TICKER, ticker)).queue();
            return;
        }
        event.reply(voteResult).queue();
    }
}
