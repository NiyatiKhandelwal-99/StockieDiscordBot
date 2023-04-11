package edu.northeastern.cs5500.starterbot.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.northeastern.cs5500.starterbot.model.UpVoteDocumentResponse;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import static com.google.common.truth.Truth.assertThat;

public class UpVoteCommandTest {

    UpVoteCommand upVoteCommand;
    CommandData commandData;

    @BeforeEach
    void setUp() {
        InMemoryRepository<UpVoteDocumentResponse> repository = new InMemoryRepository<>();

        ObjectId id1 = new ObjectId("5399aba6e4b0ae375bfdca88");

        UpVoteDocumentResponse upVoteDocumentResponse = new UpVoteDocumentResponse(id1, "aapl", 1);
        repository.add(upVoteDocumentResponse);

        ObjectId id2 = new ObjectId("5399aba6e4b0ae375bfdca89");
        UpVoteDocumentResponse upVoteDocumentResponse2 = new UpVoteDocumentResponse(id2, "aapl", 1);
        repository.add(upVoteDocumentResponse2);

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
        Document testCreateDocumentWithTicker = new UpVoteCommand().createDocumentWithTicker("aapl");
        String name = testCreateDocumentWithTicker.getString("ticker");
        assertEquals("aapl", name);
    }

    @Test
    void testCreateUpdateDocument(){
        Document testDocument = new Document("$inc", new Document("votes", 1));
        assertEquals(new UpVoteCommand().createUpDateDocument(), testDocument);
    }
}