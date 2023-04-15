package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void testCreateDocumentWithTicker() {
        Document testCreateDocumentWithTicker =
                new UpVoteCommand().createDocumentWithTicker("aapl");
        String name = testCreateDocumentWithTicker.getString("ticker");
        assertEquals("aapl", name);
    }

    @Test
    void testCreateUpdateDocument() {
        Document testDocument = new Document("$inc", new Document("votes", 1));
        assertEquals(new UpVoteCommand().createUpDateDocument(), testDocument);
    }
}
