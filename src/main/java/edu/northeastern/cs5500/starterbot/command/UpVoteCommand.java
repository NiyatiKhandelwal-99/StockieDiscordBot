package edu.northeastern.cs5500.starterbot.command;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

        if (option == null) {
            log.error(LogMessages.EMPTY_TICKER, event.getName());
            return;
        }

        String ticker = option.getAsString().toLowerCase();

        log.info("event: /upvote ticker:" + ticker);

        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();

        MongoCollection<Document> collection = mongoDatabase.getCollection("upvote");
        Document query = createDocumentWithTicker(ticker);

        // Check if the document exists with the specified ticker
        if (collectionCount(collection, query) == 1) {

            Document updateDoc = createUpDateDocument();
            collection.updateOne(query, updateDoc);
            event.reply("You have successfully upvoted for the ticker " + ticker + ".").queue();

        } else {
            event.reply(LogMessages.INVALID_TICKER).queue();
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
