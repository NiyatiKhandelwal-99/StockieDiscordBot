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
import java.util.Objects;
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

/**
 * This class represents NewsCommand, which implements SlashCommand handler and StringSelect handler
 * (for drop down list)
 */
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

    /**
     * Returns name of a command
     *
     * @return String
     */
    @Nonnull
    @Override
    public String getName() {
        return "latestnews";
    }

    /**
     * Returns the structure of a command
     *
     * @return CommandData
     */
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

    /**
     * Fetch latest news feeds for a given ticker
     *
     * @param ticker
     * @return List<AlphaVantageNewsFeed>
     * @throws RestException
     * @throws AlphaVantageException
     */
    public List<AlphaVantageNewsFeed> getNewsFeed(String ticker)
            throws RestException, AlphaVantageException {
        final String fromTime =
                LocalDateTime.now()
                        .minusDays(NUMBER_OF_DAYS)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

        return newsFeedController.getNewsFeeds(ticker, fromTime);
    }

    /**
     * This method is triggered when /latestnews command is entered
     *
     * @param event
     */
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        log.info("event: /latestnews");
        var ticker =
                Objects.requireNonNull(event.getOption("ticker", OptionMapping::getAsString))
                        .toUpperCase();

        if (ticker == null) {
            log.error(LogMessages.EMPTY_TICKER, event.getName());
            return;
        }

        log.info("event: /latestnews ticker:" + ticker);

        List<AlphaVantageNewsFeed> newsFeeds = fetchNewsFeeds(ticker);

        if (checkNewsFeeds(newsFeeds)) {
            event.reply("No response found for " + ticker).queue();
            return;
        }

        event.reply("Bringing latest news from last " + NUMBER_OF_DAYS_IN_WORDS + " days!").queue();
        /** Generating embed builders for each news feed. */
        List<MessageEmbed> newsEmbeds = renderEmbeds(newsFeeds);

        for (MessageEmbed embed : newsEmbeds) {
            if (embed != null) {
                event.getChannel().sendMessageEmbeds(embed).queue();
            }
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

    /**
     * Generates and returns drop down menu for a given list of neews feeds but excluding source
     * ticker
     *
     * @param newsFeeds
     * @param sourceTicker
     * @return StringSelectMenu
     */
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

    /**
     * Fetch news feeds for a given ticker
     *
     * @param ticker
     * @return List<AlphaVantageNewsFeed>
     */
    private List<AlphaVantageNewsFeed> fetchNewsFeeds(String ticker) {
        List<AlphaVantageNewsFeed> newsFeeds = null;
        try {
            newsFeeds = getNewsFeed(ticker);
        } catch (RestException | AlphaVantageException exp) {
            log.error(String.format(LogMessages.ERROR_ALPHAVANTAGE_API, exp.getMessage()), exp);
        }
        return newsFeeds;
    }

    /**
     * Checks if news feeds list is null or empty
     *
     * @param newsFeeds
     * @return boolean
     */
    boolean checkNewsFeeds(List<AlphaVantageNewsFeed> newsFeeds) {
        return newsFeeds == null || newsFeeds.isEmpty();
    }

    /**
     * @param newsFeeds
     * @param sourceTicker
     * @return List<String>
     */
    public List<String> createListOfTitles(
            List<AlphaVantageNewsFeed> newsFeeds, String sourceTicker) {

        return newsFeeds.stream()
                .filter(p -> !p.getTitle().contains(sourceTicker))
                .map(AlphaVantageNewsFeed::getTitle)
                .toList();
    }

    /**
     * Finds valid tickers from an active list of tickers and titles list
     *
     * @param tickerList
     * @param titles
     * @return Map<String, String>
     */
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

    /**
     * Returns active list (map: ticker as key and company name as value) of companies.
     *
     * @return Map<String, String>
     * @throws RestException
     * @throws AlphaVantageException
     * @throws IOException
     */
    public Map<String, String> getTickers()
            throws RestException, AlphaVantageException, IOException {

        return newsFeedController.getTickers();
    }

    /**
     * Returns list of message embeds to be rendered
     *
     * @param newsFeeds
     * @return List<MessageEmbed>
     */
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

    /**
     * Generates and returns a message embed for a given news feed.
     *
     * @param newsFeed
     * @return MessageEmbed
     */
    private MessageEmbed renderEmbed(AlphaVantageNewsFeed newsFeed) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(newsFeed.getTitle());
        embed.addField("Url", getValue(newsFeed.getUrl()), false);
        embed.addField("Source", getValue(newsFeed.getSource()), true);
        embed.addField("Source Domain", getValue(newsFeed.getSourceDomain()), true);
        embed.addField(
                "Category within source", getValue(newsFeed.getCategoryWithinSource()), true);
        embed.addField("Authors", getValue(String.join(", ", newsFeed.getAuthors())), true);
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

    /**
     * Returns value if not null, otherwise returns empty string
     *
     * @param value
     * @return String
     */
    @Nonnull
    public static String getValue(String value) {
        return (value == null) ? "" : value;
    }

    /**
     * Returns comma separated string of topics for a given list of alpha vantage topics
     *
     * @param list
     * @return String
     */
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

    /**
     * Returns datetime in user readable format if in correct format, otherwise returns n/a
     *
     * @param userDateTime
     * @return String
     */
    @Nonnull
    public static String formatDateTimeString(String userDateTime) {
        if (userDateTime == null || userDateTime.equals("") || userDateTime.indexOf("T") <= 0) {
            return "n/a";
        }
        SimpleDateFormat fromApi = new SimpleDateFormat("yyyyMMdd HHmmss");
        SimpleDateFormat finalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = "";
        try {
            formattedDate = finalFormat.format(fromApi.parse(userDateTime.replace('T', ' ')));
        } catch (ParseException pe) {
            log.error(String.format("Date is in wrong format %s", userDateTime), pe);
        }
        return formattedDate.trim().length() == 0 ? "n/a" : formattedDate;
    }

    /**
     * This method is triggered when a item from a drop down list is clicked.
     *
     * @param event
     */
    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        log.info(
                "In NewsCommand onStringSelectInteraction "
                        + event.getInteraction().getSelectedOptions().get(0).getValue());
        var ticker = event.getInteraction().getSelectedOptions().get(0).getValue();
        if (ticker.isEmpty() || ticker.length() == 0) {
            throw new MissingRequiredParameterException(LogMessages.EMPTY_TICKER);
        }
        List<AlphaVantageNewsFeed> newsFeeds = fetchNewsFeeds(ticker);

        if (checkNewsFeeds(newsFeeds)) {
            event.reply("No reponse found for " + ticker).queue();
            return;
        }

        event.reply("Bringing latest news from last " + NUMBER_OF_DAYS_IN_WORDS + " days!").queue();
        /** Generating embed builders for each news feed. */
        List<MessageEmbed> newsEmbeds = renderEmbeds(newsFeeds);

        for (MessageEmbed embed : newsEmbeds) {
            if (embed != null) {
                event.getChannel().sendMessageEmbeds(embed).queue();
            }
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

    /**
     * Gets a list of all menu items and generates drop down list
     *
     * @param newsFeeds
     * @param sourceTicker
     * @return StringSelectMenu
     * @throws RestException
     * @throws AlphaVantageException
     * @throws IOException
     * @throws InterruptedException
     */
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

    /**
     * Generates drop down list with max twenty-five items for unique tickers
     *
     * @param uniqueTickerLists
     * @return StringSelectMenu
     */
    public StringSelectMenu createDropDownListForNews(Map<String, String> uniqueTickerLists) {
        List<SelectOption> otherTickerNews = new ArrayList<>();
        final int MENU_LIST_MAX_ITEM = 25;
        for (Map.Entry<String, String> entry : uniqueTickerLists.entrySet()) {
            if (otherTickerNews.size() >= MENU_LIST_MAX_ITEM) {
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
