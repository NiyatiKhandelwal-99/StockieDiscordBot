package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

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
}
