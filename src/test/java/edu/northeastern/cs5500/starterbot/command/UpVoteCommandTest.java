package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpVoteCommandTest {

    UpVoteCommand upVoteCommand;
    CommandData commandData;

    @BeforeEach
    void setUp() {
        upVoteCommand = new UpVoteCommand();
        commandData = upVoteCommand.getCommandData();
    }

    @Test
    void testGetName() {
        String name = new UpVoteCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData testUpVoteCommandData = new UpVoteCommand().getCommandData();
        assertThat(testUpVoteCommandData).isNotNull();
        assertThat(testUpVoteCommandData.getName()).isEqualTo(commandData.getName());
    }
}
