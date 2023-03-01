package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.Mockito;

class PriceCommandTest {

  PriceCommand priceCommand;
  CommandData commandData;
  @BeforeEach
  void setUp() {
    priceCommand = new PriceCommand();
    commandData = priceCommand.getCommandData();
  }

  @Test
  void getName() {
    String name = priceCommand.getName();
    assertThat(name).isEqualTo(commandData.getName());
  }

  @Test
  void getCommandData() {
    CommandData commandData = priceCommand.getCommandData();
  }

  @Test
  void onSlashCommandInteraction() {

  }
}