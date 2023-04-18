package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BalanceSheetCommandTest {

    BalanceSheetCommand balanceSheetCommand;
    CommandData commandData;

    @BeforeEach
    void setup() {
        balanceSheetCommand = new BalanceSheetCommand();
        commandData = balanceSheetCommand.getCommandData();
    }

    @Test
    void testGetName() {
        String name = new BalanceSheetCommand().getName();
        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testGetCommandData() {
        CommandData testBalanceSheetCommandData = new BalanceSheetCommand().getCommandData();
        assertThat(testBalanceSheetCommandData).isNotNull();
        assertThat(testBalanceSheetCommandData.getName()).isEqualTo(commandData.getName());
    }
}
