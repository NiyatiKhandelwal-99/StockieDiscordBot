package edu.northeastern.cs5500.starterbot.command;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import edu.northeastern.cs5500.starterbot.annotate.ExcludeMethodFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.constants.LogMessages;
import edu.northeastern.cs5500.starterbot.controller.VotingController;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bson.Document;

@Singleton
@Slf4j
public class UpVoteCommand implements SlashCommandHandler {

    public static final String VOTES = "votes";
    public static final String VOTERS = "voters";
    public static final String TICKER = "ticker";

    @Inject VotingController votingController;

    @Inject
    public UpVoteCommand() {}

    @Nonnull
    @Override
    public String getName() {
        return "upvote";
    }

    @Nonnull
    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Ask the bot to upvote for a particular ticker")
                .addOption(
                        OptionType.STRING, TICKER, "The bot will upvote the provided ticker", true);
    }

    @ExcludeMethodFromGeneratedCoverage
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        log.info("event: /upvote");
        var option = event.getOption(TICKER);

        String userId = null;

        if (event.getMember() != null) {
            Member m = event.getMember();
            if (m != null) userId = m.getId();
        }

        if (option == null) {
            log.error(LogMessages.EMPTY_TICKER, event.getName());
            return;
        }

        String ticker = option.getAsString().toLowerCase();

        log.info("event: /upvote ticker:" + ticker);

        MongoDatabase mongoDatabase = votingController.getMongoDatabase();

        MongoCollection<Document> collection = mongoDatabase.getCollection(VOTES);

        Document document = collection.find(Filters.eq(TICKER, ticker)).first();
        if (document == null) {
            event.reply(LogMessages.INVALID_TICKER).queue();
        } else {
            boolean userHasVoted = hasUserVoted(document, userId);

            if (!userHasVoted) {
                votingController.upVote(collection, ticker, userId);
                event.reply("You have successfully upvoted for the ticker " + ticker + ".").queue();

            } else {
                event.reply("User has already voted for this ticker.").queue();
            }
        }
    }

    public Boolean hasUserVoted(Document document, String userId) {
        return document.getList(VOTERS, String.class).contains(userId);
    }
}
