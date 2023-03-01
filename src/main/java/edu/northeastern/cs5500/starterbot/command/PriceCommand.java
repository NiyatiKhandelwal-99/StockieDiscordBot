package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.AlphaVantageApiCall;
import edu.northeastern.cs5500.starterbot.Function;
import edu.northeastern.cs5500.starterbot.PriceApi;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

@Singleton
@Slf4j
public class PriceCommand implements SlashCommandHandler {

    @Inject
    public PriceCommand() {}

    @NotNull
    @Override
    public String getName() {
        return "price";
    }

    @NotNull
    @Override
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Ask the bot to reply with price")
                .addOption(
                        OptionType.STRING,
                        "ticker",
                        "The bot will reply to your command with the provided text",
                        true);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        log.info("event: /price");
        var option = event.getOption("ticker");

        if (option == null) {
            log.error("Received null value for mandatory parameter 'ticker'");
            return;
        }
        String response =
                callPriceAPI(Objects.requireNonNull(event.getOption("ticker")).getAsString());
        if (response.equals("")) {
            event.reply("Invalid ticker passed! Please enter correct ticker").queue();
            return;
        }
        event.reply(response).queue();
    }

    private String callPriceAPI(String ticker) {
        return new PriceApi(new AlphaVantageApiCall(Function.GLOBAL_QUOTE.name())).getPrice(ticker);
    }
}
