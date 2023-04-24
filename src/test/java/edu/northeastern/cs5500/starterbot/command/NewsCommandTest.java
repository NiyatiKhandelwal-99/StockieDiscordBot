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
    List<AlphaVantageNewsFeed> newsFeeds;
    Map<String, String> tickerList;

    @BeforeEach
    void setUp() {
        newsCommand = new NewsCommand();
        commandData = newsCommand.getCommandData();
        tickerList = createTickerList();
        newsFeeds = makeNewsFeeds();
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
        assertThat(botDateFormat).isEqualTo("n/a");

        final String userFromDateTimeWrongFormat = "02-01-2023T12-23-122";
        var result = NewsCommand.formatDateTimeString(userFromDateTimeWrongFormat);
        assertThat(result).isEqualTo("n/a");

        final String userFromDateEmpty = "";
        var emptyResult = NewsCommand.formatDateTimeString(userFromDateEmpty);
        assertThat(emptyResult).isEqualTo("n/a");

        final String userFromDateTime = "20230380122133";
        String invalidDateFormat = NewsCommand.formatDateTimeString(userFromDateTime);
        assertThat(invalidDateFormat).isEqualTo("n/a");
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

        List<MessageEmbed> newsEmbeds = newsCommand.renderEmbeds(newsFeeds);
        assertThat(newsEmbeds).isNotEmpty();
        assertThat(newsEmbeds.get(0).getTitle()).isEqualTo(newsFeeds.get(0).getTitle());
    }

    @Test
    void testCheckNewsFeeds() {
        boolean resultTrue = newsCommand.checkNewsFeeds(newsFeeds);
        assertThat(resultTrue).isFalse();
        boolean resultFalse = newsCommand.checkNewsFeeds(new ArrayList<AlphaVantageNewsFeed>());
        assertThat(resultFalse).isTrue();

        boolean resultFalseNull = newsCommand.checkNewsFeeds(null);
        assertThat(resultFalseNull).isTrue();
    }

    @Test
    void testCreateListOfTitles() {
        List<String> titles = newsCommand.createListOfTitles(newsFeeds, "GOOG");
        assertThat(titles).isNotNull();
        assertThat(titles.get(0)).contains("AMZN");
    }

    @Test
    void testFindValidTickers() {
        Map<String, String> uniqueTickerLists =
                newsCommand.findValidTickers(
                        tickerList, List.of("AAPL", "IBM", "EAST", "AMZN", "GOOG"));
        assertThat(uniqueTickerLists).isNotEmpty();
        assertThat(uniqueTickerLists.containsKey("AAPL")).isTrue();
    }

    @Test
    void testCreateDropDownListForNews() {
        StringSelectMenu menu = newsCommand.createDropDownListForNews(tickerList);
        assertThat(menu).isNotNull();
        assertThat(menu.isDisabled()).isFalse();
        assertThat(menu.getPlaceholder()).isEqualTo("Select tickers for more news");
        assertThat(menu.getOptions()).isNotEmpty();
    }

    @Test
    void testCreateDropDownListForNewsWithMaxItems() {
        Map<String, String> moreTickers = addMoreItems();
        StringSelectMenu menu = newsCommand.createDropDownListForNews(moreTickers);
        assertThat(menu).isNotNull();
        assertThat(menu.isDisabled()).isFalse();
        assertThat(menu.getPlaceholder()).isEqualTo("Select tickers for more news");
        assertThat(menu.getOptions()).isNotEmpty();
    }

    private Map<String, String> addMoreItems() {
        Map<String, String> moreItemsMap = new HashMap<>();
        moreItemsMap.put("AMZN", "Amazon");
        moreItemsMap.put("GOOG", "Google");
        moreItemsMap.put("IBM", "IBM");
        moreItemsMap.put("AAPL", "Apple");
        moreItemsMap.put("BABA", "Alibaba");
        moreItemsMap.put("A", "Agilent");
        moreItemsMap.put("AA", "Alcoa");
        moreItemsMap.put("AAA", "Axis first");
        moreItemsMap.put("AAU", "Goldman Sachs");
        moreItemsMap.put("AAC", "some example");
        moreItemsMap.put("AAC-U", "some");
        moreItemsMap.put("AAC-WS", "A test");
        moreItemsMap.put("AACG", "AAC test");
        moreItemsMap.put("AACI", "AACi test");
        moreItemsMap.put("AACIU", "AACIU test");
        moreItemsMap.put("AACIW", "AACIW test");
        moreItemsMap.put("AADI", "AADi test");
        moreItemsMap.put("AADR", "AADr test");
        moreItemsMap.put("AAIC-P-B", "AAIC-P-B test");
        moreItemsMap.put("AAIC-P-C", "AAIC-P-c test");
        moreItemsMap.put("AAIN", "AAIN test");
        moreItemsMap.put("AAL", "AAL test");
        moreItemsMap.put("AAMC", "AAMC test");
        moreItemsMap.put("AAME", "AAME test");
        moreItemsMap.put("AAN", "AAn test");
        moreItemsMap.put("AAOI", "AAOI test");
        return moreItemsMap;
    }

    private Map<String, String> createTickerList() {
        this.tickerList = new HashMap<>();
        this.tickerList.put("IBM", "IBM");
        this.tickerList.put("AMZN", "Amazon");
        this.tickerList.put("GOOG", "Google");
        this.tickerList.put("AAPL", "Apple");
        this.tickerList.put("BABA", "Alibaba");
        return this.tickerList;
    }

    private List<AlphaVantageNewsFeed> makeNewsFeeds() {
        newsFeeds = new ArrayList<>();
        final String largeString =
                "AMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's saysAMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says";
        AlphaVantageNewsTopic topics = new AlphaVantageNewsTopic("Technology");
        this.newsFeeds.add(
                new AlphaVantageNewsFeed(
                        "Alphabet CEO Sundar Pichai's pay soars to $226 mn on huge stock award IBM AAPL GOOG",
                        "https://www.financialexpress.com/market/asia-stocks-off-to-slow-start-in-earnings-rich-week/3059437/",
                        "20230322T022729",
                        new String[] {"Business Standard New"},
                        "Alphabet CEO Sundar Pichai's pay soars to $226 mn on huge stock",
                        "null",
                        "Business Standard New",
                        "www.businessstandard.com",
                        "www.businessstandard.com",
                        "0.00957",
                        "Important",
                        List.of(topics)));
        this.newsFeeds.add(
                new AlphaVantageNewsFeed(
                        "AMZN BABA Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says Apple's new savings account is more bad news for banks, Moody's says",
                        "https://www.business-standard.com/world-news/alphabet-ceo-sundar-pichai-s-pay-soars-to-226-mn-on-huge-stock-award-123042200102_1.html",
                        "20230422T022729",
                        new String[] {"Business Standard Test"},
                        "Alphabet CEO Sundar Pichai's pay soars to $226 mn on huge stock ... Business Standard ...",
                        "https://bsmedia.business-standard.com/_media/bs/img/article/2023-04/22/full/1682130354-2122.jpg?im=FeatureCrop,width=826,height=465",
                        "Business Standard Test",
                        "www.business-standardnew.com",
                        "www.business-standardnew.com",
                        "0.009576",
                        "Neutral",
                        List.of(topics)));
        this.newsFeeds.add(
                new AlphaVantageNewsFeed(
                        largeString,
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
}
