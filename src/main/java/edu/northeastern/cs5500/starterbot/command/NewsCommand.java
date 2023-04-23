package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.constants.LogMessages;
import edu.northeastern.cs5500.starterbot.controller.NewsFeedController;
import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.MissingRequiredParameterException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsTopic;
import java.awt.Color;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.Nullable;

@Singleton
@Slf4j
public class NewsCommand implements SlashCommandHandler, StringSelectHandler {

    @Inject NewsFeedController newsFeedController;
    private static final int NUMBER_OF_DAYS = 1;
    private static final String NUMBER_OF_DAYS_IN_WORDS = "two";

    @Inject
    public NewsCommand() {
        // empty constructor required for injection
    }

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

    public List<AlphaVantageNewsFeed> getNewsFeed(String ticker)
            throws RestException, AlphaVantageException {
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(NUMBER_OF_DAYS)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

        return newsFeedController.getNewsFeeds(ticker, fromTime);
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        log.info("event: /latestnews");
        var ticker = event.getOption("ticker", OptionMapping::getAsString);

        if (ticker == null) {
            log.error(LogMessages.EMPTY_TICKER, event.getName());
            return;
        }

        log.info("event: /latestnews ticker:" + ticker);

        List<AlphaVantageNewsFeed> newsFeeds = fetchNewsFeeds(ticker);

        if (checkNewsFeeds(newsFeeds)) {
            event.reply(String.format(LogMessages.EMPTY_RESPONSE, ticker)).queue();
            return;
        }

        event.reply("Bringing latest news from last " + NUMBER_OF_DAYS_IN_WORDS + " days!").queue();
        /** Generating embed builders for each news feed. */
        List<MessageEmbed> newsEmbeds = renderEmbeds(newsFeeds);

        for (MessageEmbed embed : newsEmbeds) {
            event.getChannel().sendMessageEmbeds(embed).queue();
        }

        StringSelectMenu menu = generateDropDownMenu(newsFeeds, ticker);

        if (menu == null || menu.getOptions().isEmpty()) {
            log.info("Menu list is empty");
            event.getChannel()
                    .sendMessageFormat(
                            "No new ticker found in news titles. Please use latest news command to know more news.")
                    .queue();
            return;
        }

        event.getChannel()
                .sendMessageFormat(
                        "Total "
                                + menu.getOptions().size()
                                + " different ticker added for latest news. Check them out if you like.")
                .addActionRow(menu)
                .queue();
    }

    private StringSelectMenu generateDropDownMenu(
            List<AlphaVantageNewsFeed> newsFeeds, String sourceTicker) {

        log.info("event: creating dropdown list for other tickers");

        StringSelectMenu menu = null;
        try {
            menu = getStringSelectMenu(newsFeeds, sourceTicker);
        } catch (RestException | AlphaVantageException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return menu;
    }

    private List<AlphaVantageNewsFeed> fetchNewsFeeds(String ticker) {
        List<AlphaVantageNewsFeed> newsFeeds = null;
        try {
            newsFeeds = getNewsFeed(ticker);
        } catch (RestException | AlphaVantageException exp) {
            log.error(String.format(LogMessages.ERROR_ALPHAVANTAGE_API, exp.getMessage()), exp);
        }
        return newsFeeds;
    }

    boolean checkNewsFeeds(List<AlphaVantageNewsFeed> newsFeeds) {
        return newsFeeds == null || newsFeeds.size() == 0;
    }

    public List<String> createListOfTitles(
            List<AlphaVantageNewsFeed> newsFeeds, String sourceTicker) {

        return newsFeeds.stream()
                .map(AlphaVantageNewsFeed::getTitle)
                .filter(p -> !p.contains(sourceTicker))
                .toList();
    }

    public Map<String, String> findValidTickers(
            Map<String, String> tickerList, List<String> titles) {
        Map<String, String> uniqueValidTickers = new HashMap<>();
        final String regex = "(\\b[A-Z][A-Z]+\\b)";
        final Pattern pattern = Pattern.compile(regex);
        for (String title : titles) {
            final Matcher matcher = pattern.matcher(title);
            while (matcher.find()) {
                if (tickerList.containsKey(matcher.group(0))) {
                    uniqueValidTickers.put(matcher.group(0), tickerList.get(matcher.group(0)));
                }
            }
        }
        return uniqueValidTickers;
    }

    public Map<String, String> getTickers()
            throws RestException, AlphaVantageException, IOException {

        return newsFeedController.getTickers();
    }

    public List<MessageEmbed> renderEmbeds(List<AlphaVantageNewsFeed> newsFeeds) {
        List<MessageEmbed> newsEmbeds = new ArrayList<>();
        int TITLE_MAX_LENGTH = 255;

        for (AlphaVantageNewsFeed newsFeed : newsFeeds) {
            if (newsFeed.getTitle().length() > TITLE_MAX_LENGTH) {
                continue;
            }
            newsEmbeds.add(renderEmbed(newsFeed));
        }

        return newsEmbeds;
    }

    private MessageEmbed renderEmbed(AlphaVantageNewsFeed newsFeed) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(newsFeed.getTitle());
        embed.addField("Url", getValue(newsFeed.getUrl()), false);
        embed.addField("Source", getValue(newsFeed.getSource()), true);
        embed.addField("Source Domain", getValue(newsFeed.getSourceDomain()), true);
        embed.addField(
                "Category within source", getValue(newsFeed.getCategoryWithinSource()), true);
        embed.addField("Authors", String.join(", ", newsFeed.getAuthors()), true);
        embed.addField(
                "Time published",
                formatDateTimeString(newsFeed.getTimePublished()),
                true); // 20230319T122100
        embed.addField("Topics", formatTopicString(newsFeed.getTopics()), false);
        embed.setColor(Color.orange);
        if (newsFeed.getBannerImage() != null
                && EmbedBuilder.URL_PATTERN.matcher(newsFeed.getBannerImage()).matches()) {
            embed.setThumbnail(newsFeed.getBannerImage());
        }
        return embed.build();
    }

    @Nonnull
    public static String getValue(String value) {
        return (value == null) ? "" : value;
    }

    @Nonnull
    public static String formatTopicString(List<AlphaVantageNewsTopic> list) {
        if (list == null || list.isEmpty()) {
            return "Topics not provided";
        }
        StringBuilder topicString = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null
                    || list.get(i).getTopic() == null
                    || list.get(i).getTopic().equals("")) {
                continue;
            }
            String topic = list.get(i).getTopic();
            topicString.append(topic);
            if (i != list.size() - 1) {
                topicString.append(", ");
            }
        }
        return topicString.toString();
    }

    @Nonnull
    public static String formatDateTimeString(String userDateTime) {
        if (userDateTime == null || userDateTime.equals("") || userDateTime.indexOf("T") <= 0) {
            return "";
        }
        SimpleDateFormat fromApi = new SimpleDateFormat("yyyyMMdd HHmmss");
        SimpleDateFormat finalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = "";
        try {
            formattedDate = finalFormat.format(fromApi.parse(userDateTime.replace('T', ' ')));
        } catch (ParseException pe) {
            log.error(String.format("Date is in wrong format %s", userDateTime), pe);
        }
        return formattedDate;
    }

    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        log.info(
                "In NewsCommand onStringSelectInteraction "
                        + event.getInteraction().getSelectedOptions().get(0).getValue());
        var ticker = event.getInteraction().getSelectedOptions().get(0).getValue();
        if (ticker == null || ticker.length() == 0) {
            throw new MissingRequiredParameterException(LogMessages.EMPTY_TICKER);
        }
        List<AlphaVantageNewsFeed> newsFeeds = fetchNewsFeeds(ticker);

        if (checkNewsFeeds(newsFeeds)) {
            event.reply(String.format(LogMessages.EMPTY_RESPONSE, ticker)).queue();
            return;
        }

        event.reply("Bringing latest news from last " + NUMBER_OF_DAYS_IN_WORDS + " days!").queue();
        /** Generating embed builders for each news feed. */
        List<MessageEmbed> newsEmbeds = renderEmbeds(newsFeeds);

        for (MessageEmbed embed : newsEmbeds) {
            event.getChannel().sendMessageEmbeds(embed).queue();
        }

        StringSelectMenu menu = generateDropDownMenu(newsFeeds, ticker);

        if (menu == null || menu.getOptions().isEmpty()) {
            log.info("Menu list is empty");
            event.getChannel()
                    .sendMessageFormat(
                            "No new ticker found in news titles. Please use latest news command to know more news.")
                    .queue();
            return;
        }
        event.getChannel()
                .sendMessageFormat(
                        "Total "
                                + menu.getOptions().size()
                                + " different ticker added for latest news. Check them out if you like.")
                .addActionRow(menu)
                .queue();
    }

    @Nullable
    public StringSelectMenu getStringSelectMenu(
            List<AlphaVantageNewsFeed> newsFeeds, String sourceTicker)
            throws RestException, AlphaVantageException, IOException, InterruptedException {

        List<String> titles = createListOfTitles(newsFeeds, sourceTicker);

        if (titles == null || titles.isEmpty()) {
            return null;
        }

        Map<String, String> tickerList = getTickers();

        Map<String, String> uniqueTickerLists = findValidTickers(tickerList, titles);

        if (uniqueTickerLists.isEmpty()) {
            log.error("No response found for other tickers from news feeds.");
            return null;
        }

        return createDropDownListForNews(uniqueTickerLists);
    }

    public StringSelectMenu createDropDownListForNews(Map<String, String> uniqueTickerLists) {
        List<SelectOption> otherTickerNews = new ArrayList<>();
        final int DROP_DOWN_MAX_LENGTH = 25;
        for (Map.Entry<String, String> entry : uniqueTickerLists.entrySet()) {
            if (otherTickerNews.size() >= DROP_DOWN_MAX_LENGTH) {
                break;
            }
            otherTickerNews.add(
                    SelectOption.of(entry.getKey() + " : " + entry.getValue(), entry.getKey()));
        }

        return StringSelectMenu.create("latestnews")
                .setPlaceholder("Select tickers for more news")
                .addOptions(otherTickerNews)
                .build();
    }
}
