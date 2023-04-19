package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.AlphaVantageGlobalQuote;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PriceCommandTest {

    PriceCommand priceCommand;
    CommandData commandData;

    @BeforeEach
    void setUp() {
        priceCommand = new PriceCommand();
        commandData = priceCommand.getCommandData();
    }

    @Test
    void testGetName() {
        String name = new PriceCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData testPriceCommandData = new PriceCommand().getCommandData();
        assertThat(testPriceCommandData).isNotNull();
        assertThat(testPriceCommandData.getName()).isEqualTo(commandData.getName());
    }

    @Test
    void testFormatMessageContainsCurrentPrice() {
        AlphaVantageGlobalQuote quote =
                new AlphaVantageGlobalQuote(
                        null,
                        null,
                        null,
                        null,
                        "PRICE_PLACEHOLDER_VALUE",
                        null,
                        null,
                        null,
                        null,
                        null);
        String message = PriceCommand.formatMessage(quote);
        assertThat(message).contains("PRICE_PLACEHOLDER_VALUE");
    }

    @Test
    void testFormatMessageContainsLatestTradingDay() {
        AlphaVantageGlobalQuote quote =
                new AlphaVantageGlobalQuote(
                        null,
                        null,
                        null,
                        null,
                        "PRICE_PLACEHOLDER_VALUE",
                        null,
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                        null,
                        null,
                        null);
        String message = PriceCommand.formatMessage(quote);
        assertThat(message).contains("PRICE_PLACEHOLDER_VALUE");
    }
}
