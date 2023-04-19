package edu.northeastern.cs5500.starterbot.command;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.northeastern.cs5500.starterbot.annotate.ExcludeClassFromGeneratedCoverage;

@Module
@ExcludeClassFromGeneratedCoverage
public class CommandModule {
    @Provides
    @IntoSet
    public SlashCommandHandler providePriceCommand(PriceCommand priceCommand) {
        return priceCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideNewsCommand(NewsCommand newsCommand) {
        return newsCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideBalanceSheetCommand(BalanceSheetCommand balanceSheetCommand) {
        return balanceSheetCommand;
    }
}
