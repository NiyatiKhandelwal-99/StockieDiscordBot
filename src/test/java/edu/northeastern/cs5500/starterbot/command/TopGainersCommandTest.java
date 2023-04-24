package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import java.util.Map;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TopGainersCommandTest {

    TopGainersCommand topGainersCommand;
    CommandData commandData;

    Map<String, String> topGainers;

    @BeforeEach
    void setup() {
        topGainersCommand = new TopGainersCommand();
        commandData = topGainersCommand.getCommandData();

        topGainers =
                Map.of(
                        "AAPL", "+10.45", "GOOG", "+32.7", "TSLA", "+100.21", "MSFT", "+23.4",
                        "AMZN", "+2.45", "MS", "+34.8");
    }

    @Test
    void testGetCommandData() {
        CommandData testTopGainersCommandData = new TopGainersCommand().getCommandData();
        assertThat(testTopGainersCommandData).isNotNull();
        assertThat(testTopGainersCommandData.getName()).isEqualTo(commandData.getName());
    }

    @Test
    void testGetName() {
        String name = new TopGainersCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testGetTopGainers() {}

    @Test
    void testRenderTopGainers() {}
}
