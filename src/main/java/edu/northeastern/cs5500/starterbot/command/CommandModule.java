package edu.northeastern.cs5500.starterbot.command;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class CommandModule {

    @Provides
    @IntoSet
    public SlashCommandHandler providePriceCommand(PriceCommand priceCommand) {
        return priceCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideUpVoteCommand(UpVoteCommand upVoteCommand) {
        return upVoteCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideDownVoteCommand(DownVoteCommand downVoteCommand) {
        return downVoteCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideNewsCommand(NewsCommand newsCommand) {
        return newsCommand;
    }

    /**
     * provideBalanceSheetCommand function is responsible for providing the balanceSheetCommand object when
     * the /balance command is executed by the user.
     * 
     * @param balanceSheetCommand
     * @return SlashCommandHandler
     */
    @Provides
    @IntoSet
    public SlashCommandHandler provideBalanceSheetCommand(BalanceSheetCommand balanceSheetCommand) {
        return balanceSheetCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideIncomeStatementCommand(
            IncomeStatementCommand incomeStatementCommand) {
        return incomeStatementCommand;
    }

    /**
     * provideGainersCommand function is responsible for providing the topGainersCommand object when
     * the /gainers command is executed by the user.
     * 
     * @param topGainersCommand
     * @return SlashCommandHandler
     */
    @Provides
    @IntoSet
    public SlashCommandHandler provideGainersCommand(TopGainersCommand topGainersCommand) {
        return topGainersCommand;
    }

    /**
     * provideLosersCommand function is responsible for providing the topLosersCommand object when
     * the /losers command is executed by the user.
     * 
     * @param topGainersCommand
     * @return SlashCommandHandler
     */
    @Provides
    @IntoSet
    public SlashCommandHandler provideLosersCommand(TopLosersCommand topLosersCommand) {
        return topLosersCommand;
    }

    @Provides
    @IntoSet
    public StringSelectHandler provideNewsStringSelectCommand(NewsCommand newsCommand) {
        return newsCommand;
    }
}
