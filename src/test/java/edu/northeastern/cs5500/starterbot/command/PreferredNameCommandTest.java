package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PreferredNameCommandTest {
    PreferredNameCommand preferredNameCommand;
    CommandData commandData;

    @BeforeEach
    void setUp() {
        preferredNameCommand = new PreferredNameCommand();
        commandData = preferredNameCommand.getCommandData();
    }

    @Test
    void testGetName() {
        String name = new PreferredNameCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData testPreferredNameCommandData = new PreferredNameCommand().getCommandData();
        assertThat(testPreferredNameCommandData).isNotNull();
        assertThat(testPreferredNameCommandData.getName()).isEqualTo(commandData.getName());
    }
}
