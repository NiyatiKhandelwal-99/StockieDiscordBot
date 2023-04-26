package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import java.awt.Color;
import java.util.Map;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TopLosersCommandTest {

    TopLosersCommand topLosersCommand;
    CommandData commandData;

    Map<String, String> topLosers;

    @BeforeEach
    void setup() {
        topLosersCommand = new TopLosersCommand();
        commandData = topLosersCommand.getCommandData();

        topLosers =
                Map.of(
                        "AAPL", "-10.45", "GOOG", "-32.7", "TSLA", "-100.21", "MSFT", "-23.4",
                        "AMZN", "-2.45", "MS", "-34.8");
    }

    @Test
    void testGetCommandData() {
        CommandData testTopLosersCommandData = new TopLosersCommand().getCommandData();
        assertThat(testTopLosersCommandData).isNotNull();
        assertThat(testTopLosersCommandData.getName()).isEqualTo(commandData.getName());
    }

    @Test
    void testGetName() {
        String name = new TopLosersCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testTopLosersEmbed() {
        MessageEmbed messageEmbded = topLosersCommand.getTopLosersEmbed(topLosers);
        assertThat(messageEmbded.isEmpty()).isFalse();
        assertThat(messageEmbded.getTitle()).isNotEmpty();
        assertThat(messageEmbded.getColor()).isEqualTo(Color.RED);
    }
}
