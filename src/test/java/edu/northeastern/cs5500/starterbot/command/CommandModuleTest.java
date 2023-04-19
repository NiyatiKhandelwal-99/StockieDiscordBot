package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

public class CommandModuleTest {

    @Test
    public void testProvideSayCommand() {
        assertThat(new CommandModule().provideSayCommand(new ButtonCommand())).isNotNull();
    }

    @Test
    public void testProvidePreferredNameCommand() {
        assertThat(new CommandModule().providePreferredNameCommand(new PreferredNameCommand()))
                .isNotNull();
    }

    @Test
    public void testProvideButtonCommand() {
        assertThat(new CommandModule().provideButtonCommand(new ButtonCommand())).isNotNull();
    }

    @Test
    public void testProvideDropdownCommand() {
        assertThat(new CommandModule().provideDropdownCommand(new DropdownCommand())).isNotNull();
    }

    @Test
    public void testProvidePriceCommand() {
        assertThat(new CommandModule().providePriceCommand(new PriceCommand())).isNotNull();
    }

    @Test
    public void testProvideNewsCommand() {
        assertThat(new CommandModule().provideNewsCommand(new NewsCommand())).isNotNull();
    }

    @Test
    public void testProvideButtonCommandClickHandler() {
        assertThat(new CommandModule().provideButtonCommandClickHandler(new ButtonCommand()))
                .isNotNull();
    }

    @Test
    public void testProvideDropdownCommandMenuHandler() {
        assertThat(new CommandModule().provideDropdownCommandMenuHandler(new DropdownCommand()))
                .isNotNull();
    }
}
