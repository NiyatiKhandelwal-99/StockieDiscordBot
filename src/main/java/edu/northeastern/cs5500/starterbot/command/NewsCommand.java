package edu.northeastern.cs5500.starterbot.command;

import static edu.northeastern.cs5500.starterbot.constants.LogMessages.ERROR_ALPHAVANTAGE_API_REPLY;
import static edu.northeastern.cs5500.starterbot.constants.LogMessages.INVALID_TICKER;

import edu.northeastern.cs5500.starterbot.constants.LogMessages;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageApi;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageNewsFeed;
import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
public class NewsCommand implements SlashCommandHandler {

    @Inject AlphaVantageApi alphaVantageApi;
    private static final int NUMBER_OF_DAYS = 4;
    private static final String NUMBER_OF_DAYS_IN_WORDS = "four";

    @Inject
    public NewsCommand() {}

    @Nonnull
    @Override
    public String getName() {
        return "latestnews";
    }

    @Nonnull
    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Ask the bot to reply with news")
                .addOption(
                        OptionType.STRING,
                        "ticker",
                        "The bot will reply to your command with the provided text",
                        true);
    }

    AlphaVantageNewsFeed[] getNewsFeed(String ticker) throws AlphaVantageException {
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(NUMBER_OF_DAYS)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'THHmm"));

        return alphaVantageApi.getNewsSentiment(ticker, fromTime);
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        log.info("event: /latestnews");
        var option = event.getOption("ticker");

        if (option == null) {
            log.error(LogMessages.EMPTY_TICKER.toString(), event.getName());
            return;
        }

        String ticker = option.getAsString();

        log.info("event: /latestnews ticker:" + ticker);

        AlphaVantageNewsFeed[] newsFeeds = null;
        try {
            newsFeeds = getNewsFeed(ticker);
        } catch (AlphaVantageException e) {
            log.error(LogMessages.ERROR_ALPHAVANTAGE_API_REPLY.toString(), e);
            event.reply(ERROR_ALPHAVANTAGE_API_REPLY.toString()).queue();
            return;
        }

        if (newsFeeds == null) {
            event.reply(INVALID_TICKER.toString()).queue();
            return;
        }

        event.reply("Bringing latest news from last " + NUMBER_OF_DAYS_IN_WORDS + " days!").queue();
        /** Generating embed builders for each news feed. */
        ArrayList<MessageEmbed> newsEmbeds = renderEmbeds(newsFeeds);

        for (MessageEmbed embed : newsEmbeds) {
            event.getChannel().sendMessageEmbeds(embed).queue();
        }
    }

    private ArrayList<MessageEmbed> renderEmbeds(AlphaVantageNewsFeed[] newsFeeds) {
        ArrayList<MessageEmbed> newsEmbeds = new ArrayList<>();

        for (AlphaVantageNewsFeed newsFeed : newsFeeds) {
            newsEmbeds.add(renderEmbed(newsFeed));
        }

        return newsEmbeds;
    }

    private MessageEmbed renderEmbed(AlphaVantageNewsFeed newsFeed) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(newsFeed.getTitle());
        embed.setDescription(newsFeed.getSummary());
        embed.addField("Url", newsFeed.getUrl(), false);
        String[] timePublished = newsFeed.getTimePublished().split("T");
        embed.addField(
                "Time published",
                timePublished[0] + " " + timePublished[1],
                false); // 20230319T122100
        embed.addField("Authors", String.join(", ", newsFeed.getAuthors()), false);
        embed.addField("Source", newsFeed.getSource(), false);
        embed.addField("Source Domain", newsFeed.getSourceDomain(), false);
        embed.addField("Category within source", newsFeed.getCategoryWithinSource(), false);
        embed.addField(
                "Topics",
                String.join(
                        ", ", Arrays.stream(newsFeed.getTopics()).map(i -> i.getTopic()).toList()),
                false);
        embed.setColor(Color.orange);
        embed.setThumbnail(newsFeed.getBannerImage());
        return embed.build();
    }
}
