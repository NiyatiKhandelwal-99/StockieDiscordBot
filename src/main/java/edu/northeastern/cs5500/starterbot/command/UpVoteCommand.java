package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.QuoteController;
import edu.northeastern.cs5500.starterbot.exception.MissingRequiredParameterException;
import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.exception.rest.NotFoundException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.MongoDBService;
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

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class UpVoteCommand implements SlashCommandHandler {
    @Inject QuoteController quoteController;

    @Inject
    public UpVoteCommand() {
        // empty constructor required for injection
    }

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

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /upvote");
        var option = event.getOption("ticker");

        if (option == null) {
            throw new MissingRequiredParameterException("ticker");
        }

        String tickerSymbol = option.getAsString();

        MongoDBService mongoDBService = new MongoDBService();
        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();

        MongoCollection<Document> collection = mongoDatabase.getCollection("upvote");
        Document document = collection.find(eq("_id", tickerSymbol)).first();

        if(document == null){
            event.reply("Ticker does not exist!").queue();
        }
        else{
            String voteString = document.get("vote")+"";
            int vote = Integer.parseInt(voteString);
            
            UpdateResult updateResult = collection.updateOne( eq("_id", tickerSymbol), set("vote", vote+1));
            String message="";
            if(updateResult.getModifiedCount()>0)
            {
                message = "You have successfully upvoted for the ticker " +tickerSymbol+ ".";
                event.reply(message).queue();
            }
            else{
                message= "There was a problem encountered. Could not up-vote for " +tickerSymbol+ ".";
                event.reply(message).queue();
            }
        }
    }
}