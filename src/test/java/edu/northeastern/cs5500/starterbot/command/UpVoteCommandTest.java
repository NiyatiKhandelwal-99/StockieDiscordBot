package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.bson.Document;
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

    @Test
    void testHasUserVoted() {
        Document document = new Document();
        document.append("voters", Arrays.asList("user1", "user2"));
        assertEquals(true, new UpVoteCommand().hasUserVoted(document, "user1"));
        assertEquals(false, new UpVoteCommand().hasUserVoted(document, "user4"));
    }
}
