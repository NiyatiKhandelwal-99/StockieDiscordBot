package edu.northeastern.cs5500.starterbot.command;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import edu.northeastern.cs5500.starterbot.annotate.Generated;
import edu.northeastern.cs5500.starterbot.constants.LogMessages;
import edu.northeastern.cs5500.starterbot.service.MongoDBService;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bson.Document;

@Singleton
@Slf4j
public class UpVoteCommand implements SlashCommandHandler {

    @Inject MongoDBService mongoDBService;

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
                        OptionType.STRING,
                        "ticker",
                        "The bot will upvote the provided ticker",
                        true);
    }

    @Generated
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        log.info("event: /upvote");
        var option = event.getOption("ticker");
        
        String userId = event.getMember().getId();

        if (option == null) {
            log.error(LogMessages.EMPTY_TICKER, event.getName());
            return;
        }

        String ticker = option.getAsString().toLowerCase();

        log.info("event: /upvote ticker:" + ticker);

        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();

        MongoCollection<Document> collection = mongoDatabase.getCollection("votes");

        Document document = collection.find(Filters.eq("ticker", ticker)).first();
        if (document == null) {
            event.reply(LogMessages.INVALID_TICKER).queue();
        } else {
            // Check if the user has already voted for this ticker
            boolean userHasVoted = document.getList("voters", String.class).contains(userId);

            if (!userHasVoted) {
                // The user has not voted for this ticker, so update the votes and add the user to the voters list
                collection.updateOne(Filters.eq("ticker", ticker), Updates.combine(
                    Updates.inc("votes", 1),
                    Updates.addToSet("voters", userId)
                ));

                event.reply("You have successfully upvoted for the ticker " + ticker + ".").queue();

            } else {
                event.reply("User has already voted for this ticker.").queue();
            }
        }
    }

    public Document createDocumentWithTicker(String ticker) {
        return new Document("ticker", ticker);
    }

    @Generated
    public int collectionCount(MongoCollection<Document> collection, Document doc) {
        return (int) collection.countDocuments(doc);
    }

    public Document createUpDateDocument() {
        return new Document("$inc", new Document("votes", 1));
    }
}