package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageException;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageNewsTopic;
import java.util.List;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
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
        final String userFromDateTime = "20230319122133";
        String botDateFormat = NewsCommand.formatDateTimeString(userFromDateTime);
        assertThat(botDateFormat.isBlank()).isTrue();
    }

    @Test
    void testFormatDateInvalidFormat() throws RestException, AlphaVantageException {
        final String userFromDateTime = "20230380122133";
        String botDateFormat = NewsCommand.formatDateTimeString(userFromDateTime);
        assertThat(botDateFormat.isBlank()).isTrue();
    }

    @Test
    void testformatTopicString() throws RestException, AlphaVantageException {
        List<AlphaVantageNewsTopic> topicList =
                List.of(
                        new AlphaVantageNewsTopic("technology"),
                        new AlphaVantageNewsTopic("science"));
        String csvTopicString = NewsCommand.formatTopicString(topicList);
        assertThat(csvTopicString).isNotEmpty();
        assertThat(csvTopicString.equals("technology, science")).isTrue();
    }

    @Test
    void testformatTopicStringInvalidList() throws RestException, AlphaVantageException {
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
}
