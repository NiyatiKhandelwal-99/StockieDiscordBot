package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.exception.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsFeed;
import edu.northeastern.cs5500.starterbot.model.AlphaVantageNewsTopic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NewsCommandTest {
    NewsCommand newsCommand;
    CommandData commandData;

    @BeforeEach
    void setUp() {
        newsCommand = new NewsCommand();
        commandData = newsCommand.getCommandData();
    }

    @Test
    void testGetName() {
        String name = new NewsCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData testNewsCommandData = new NewsCommand().getCommandData();
        assertThat(testNewsCommandData).isNotNull();
        assertThat(testNewsCommandData.getName()).isEqualTo(commandData.getName());
    }

    @Test
    void testGetValue() {
        String value = NewsCommand.getValue("testValue");
        assertThat(value).isNotNull();
        assertThat(value.equals("testValue")).isTrue();
    }

    @Test
    void testGetInvalidValue() {
        String value = NewsCommand.getValue(null);
        assertThat(value.isBlank()).isTrue();
        ;
    }

    @Test
    void testFormatDateTimeString() throws RestException, AlphaVantageException {
        final String userFromDateTime = "20230331T111041";
        String botDateFormat = NewsCommand.formatDateTimeString(userFromDateTime);
        assertThat(botDateFormat).isNotNull();
        assertThat(botDateFormat.equals("2023-03-31 11:10:41")).isTrue();
    }

    @Test
    void testFormatDateInvalid() throws RestException, AlphaVantageException {
        final String userFromDateTimeWithoutT = "20230319122133";
        String botDateFormat = NewsCommand.formatDateTimeString(userFromDateTimeWithoutT);
        assertThat(botDateFormat.isBlank()).isTrue();

        final String userFromDateTimeWrongFormat = "02-01-2023T12-23-122";
        var result = NewsCommand.formatDateTimeString(userFromDateTimeWrongFormat);
        assertThat(result).isEmpty();

        final String userFromDateEmpty = "";
        var emptyResult = NewsCommand.formatDateTimeString(userFromDateEmpty);
        assertThat(emptyResult).isEmpty();
    }

    @Test
    void testFormatDateInvalidFormat() throws RestException, AlphaVantageException {
        final String userFromDateTime = "20230380122133";
        String botDateFormat = NewsCommand.formatDateTimeString(userFromDateTime);
        assertThat(botDateFormat.isBlank()).isTrue();
    }

    @Test
    void testFormatTopicString() throws RestException, AlphaVantageException {
        List<AlphaVantageNewsTopic> topicList =
                List.of(
                        new AlphaVantageNewsTopic("technology"),
                        new AlphaVantageNewsTopic("science"));
        String csvTopicString = NewsCommand.formatTopicString(topicList);
        assertThat(csvTopicString).isNotEmpty();
        assertThat(csvTopicString.equals("technology, science")).isTrue();
    }

    @Test
    void testFormatTopicStringInvalidList() throws RestException, AlphaVantageException {
        List<AlphaVantageNewsTopic> topicListEmpty = null;
        String csvTopicString = NewsCommand.formatTopicString(topicListEmpty);
        assertThat(csvTopicString).isNotEmpty();
        assertThat(csvTopicString.equals("Topics not provided")).isTrue();

        List<AlphaVantageNewsTopic> topicListNull = List.of(new AlphaVantageNewsTopic(null));
        String csvTopicStringNull = NewsCommand.formatTopicString(topicListNull);
        assertThat(csvTopicStringNull.isBlank()).isTrue();

        List<AlphaVantageNewsTopic> topicListValEmpty = List.of(new AlphaVantageNewsTopic(""));
        String csvTopicStringEmpty = NewsCommand.formatTopicString(topicListValEmpty);
        assertThat(csvTopicStringEmpty.isBlank()).isTrue();
    }

    @Test
    void testRenderEmbeds() {
        List<AlphaVantageNewsFeed> newsFeeds = makeNewsFeeds();
        List<MessageEmbed> newsEmbeds = newsCommand.renderEmbeds(newsFeeds);
        assertThat(newsEmbeds).isNotEmpty();
        assertThat(newsEmbeds.get(0).getTitle()).isEqualTo(newsFeeds.get(0).getTitle());
    }

    @Test
    void testCheckNewsFeeds() {
        List<AlphaVantageNewsFeed> newsFeeds = makeNewsFeeds();
        boolean resultTrue = newsCommand.checkNewsFeeds(newsFeeds);
        assertThat(resultTrue).isFalse();
        boolean resultFalse = newsCommand.checkNewsFeeds(new ArrayList<AlphaVantageNewsFeed>());
        assertThat(resultFalse).isTrue();

        boolean resultFalseNull = newsCommand.checkNewsFeeds(null);
        assertThat(resultFalseNull).isTrue();
    }

    @Test
    void testCreateListOfTitles() {
        List<AlphaVantageNewsFeed> newsFeeds = makeNewsFeeds();
        List<String> titles = newsCommand.createListOfTitles(newsFeeds);
        assertThat(titles).isNotNull();
        assertThat(titles.get(0))
                .isEqualTo(
                        "Alphabet CEO Sundar Pichai's pay soars to $226 mn on huge stock award IBM AAPL GOOG");
    }

    @Test
    void testFindValidTickers() {
        Map<String, String> tickerList = createTickerList();
        Map<String, String> uniqueTickerLists =
                newsCommand.findValidTickers(
                        tickerList, List.of("AAPL", "IBM", "EAST", "AMZN", "GOOG"));
        assertThat(uniqueTickerLists).isNotEmpty();
        assertThat(uniqueTickerLists.containsKey("AAPL")).isTrue();
    }

    @Test
    void testCreateDropDownListForNews() {
        Map<String, String> tickerList = createTickerList();
        StringSelectMenu menu = newsCommand.createDropDownListForNews(tickerList);
        assertThat(menu).isNotNull();
        assertThat(menu.isDisabled()).isFalse();
        assertThat(menu.getPlaceholder()).isEqualTo("News from other tickers!");
        assertThat(menu.getOptions()).isNotEmpty();
    }

    private Map<String, String> createTickerList() {
        Map<String, String> tickerList = new HashMap<>();
        tickerList.put("IBM", "IBM");
        tickerList.put("AMZN", "Amazon");
        tickerList.put("GOOG", "Google");
        tickerList.put("AAPL", "Apple");
        tickerList.put("BABA", "Alibaba");
        return tickerList;
    }

    private List<AlphaVantageNewsFeed> makeNewsFeeds() {
        List<AlphaVantageNewsFeed> newsFeeds = new ArrayList<>();
        AlphaVantageNewsTopic topics = new AlphaVantageNewsTopic("Technology");
        newsFeeds.add(
                new AlphaVantageNewsFeed(
                        "Alphabet CEO Sundar Pichai's pay soars to $226 mn on huge stock award IBM AAPL GOOG",
                        "https://www.business-standard.com/world-news/alphabet-ceo-sundar-pichai-s-pay-soars-to-226-mn-on-huge-stock-award-123042200102_1.html",
                        "20230422T022729",
                        new String[] {"Business Standard"},
                        "Alphabet CEO Sundar Pichai's pay soars to $226 mn on huge stock ... Business Standard ...",
                        "null",
                        "Business Standard",
                        "www.business-standard.com",
                        "www.business-standard.com",
                        "0.009576",
                        "Neutral",
                        List.of(topics)));
        newsFeeds.add(
                new AlphaVantageNewsFeed(
                        "AMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says",
                        "https://www.business-standard.com/world-news/alphabet-ceo-sundar-pichai-s-pay-soars-to-226-mn-on-huge-stock-award-123042200102_1.html",
                        "20230422T022729",
                        new String[] {"Business Standard"},
                        "Alphabet CEO Sundar Pichai's pay soars to $226 mn on huge stock ... Business Standard ...",
                        "https://bsmedia.business-standard.com/_media/bs/img/article/2023-04/22/full/1682130354-2122.jpg?im=FeatureCrop,width=826,height=465",
                        "Business Standard",
                        "www.business-standard.com",
                        "www.business-standard.com",
                        "0.009576",
                        "Neutral",
                        List.of(topics)));
        newsFeeds.add(
                new AlphaVantageNewsFeed(
                        largeTitleString(),
                        "https://www.business-standard.com/world-news/alphabet-ceo-sundar-pichai-s-pay-soars-to-226-mn-on-huge-stock-award-123042200102_1.html",
                        "20230422T022729",
                        new String[] {"Business Standard"},
                        "Alphabet CEO Sundar Pichai's pay soars to $226 mn on huge stock ... Business Standard ...",
                        "https://bsmedia.business-standard.com/_media/bs/img/article/2023-04/22/full/1682130354-2122.jpg?im=FeatureCrop,width=826,height=465",
                        "Business Standard",
                        "www.business-standard.com",
                        "www.business-standard.com",
                        "0.009576",
                        "Neutral",
                        List.of(topics)));
        return newsFeeds;
    }

    private String largeTitleString() {
        return "AMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says";
    }
}
