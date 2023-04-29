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

/**
 * DownVoteCommand is responsible for handling the /downvote commands and rendering them on the
 * Discord UI The DownVoteCommand transfers the event details to the controller for further
 * processing.
 */
@Singleton
@Slf4j
public class DownVoteCommand implements SlashCommandHandler {

    public static final String VOTES = "votes";
    public static final String VOTERS = "voters";
    public static final String TICKER = "ticker";

    @Inject VotingController votingController;

    @Inject
    public DownVoteCommand() {/* This constructor is required to facilitate injection. */}

    /**
     * Returns the name of a command
     *
     * @return String : Name of command
     */
    @Nonnull
    @Override
    public String getName() {
        return "downvote";
    }

    /**
     * Returns the structure of the command
     *
     * @return String : Format of the slash command
     */
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

    /**
     * onSlashCommandInteraction is triggered when /downvote command is entered by the user
     *
     * @param event
     */
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
