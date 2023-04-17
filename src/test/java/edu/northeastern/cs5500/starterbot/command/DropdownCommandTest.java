package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DropdownCommandTest {
    DropdownCommand dropdownCommand;
    CommandData commandData;

    @BeforeEach
    void setUp() {
        dropdownCommand = new DropdownCommand();
        commandData = dropdownCommand.getCommandData();
    }

    @Test
    void testGetName() {
        String name = new DropdownCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData testDropdownCommandData = new DropdownCommand().getCommandData();
        assertThat(testDropdownCommandData).isNotNull();
        assertThat(testDropdownCommandData.getName()).isEqualTo(commandData.getName());
    }
}
