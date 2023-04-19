package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ButtonCommandTest {
    ButtonCommand buttonCommand;
    CommandData commandData;

    @BeforeEach
    void setUp() {
        buttonCommand = new ButtonCommand();
        commandData = buttonCommand.getCommandData();
    }

    @Test
    void testGetName() {
        String name = new ButtonCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData testButtonCommandData = new ButtonCommand().getCommandData();
        assertThat(testButtonCommandData).isNotNull();
        assertThat(testButtonCommandData.getName()).isEqualTo(commandData.getName());
    }
}
