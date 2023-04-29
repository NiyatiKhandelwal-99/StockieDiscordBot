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

    /**
     * provideUpVoteCommand function is responsible for providing the upVoteCommand object when the
     * /upvote command is executed by the user.
     *
     * @param upVoteCommand
     * @return SlashCommandHandler
     */
    @Provides
    @IntoSet
    public SlashCommandHandler provideUpVoteCommand(UpVoteCommand upVoteCommand) {
        return upVoteCommand;
    }

    /**
     * provideDownVoteCommand function is responsible for providing the downVoteCommand object when
     * the /downvote command is executed by the user.
     *
     * @param downVoteCommand
     * @return SlashCommandHandler
     */
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
     * provideBalanceSheetCommand function is responsible for providing the balanceSheetCommand
     * object when the /balance command is executed by the user.
     *
     * @param balanceSheetCommand
     * @return SlashCommandHandler
     */
    @Provides
    @IntoSet
    public SlashCommandHandler provideBalanceSheetCommand(BalanceSheetCommand balanceSheetCommand) {
        return balanceSheetCommand;
    }

    /**
     * provideIncomeStatementCommand function is responsible for providing the
     * IncomeStatementCommand object when the /income command is executed by the user.
     *
     * @param incomeStatementCommand
     * @return SlashCommandHandler
     */
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

    /**
     * provideWinningPortfoliosCommand function is responsible for providing the
     * winningPortfoliosCommand object when the /losers command is executed by the user.
     *
     * @param winningPortfoliosCommand
     * @return SlashCommandHandler
     */
    @Provides
    @IntoSet
    public SlashCommandHandler provideWinningPortfoliosCommand(
            WinningPortfoliosCommand winningPortfoliosCommand) {
        return winningPortfoliosCommand;
    }

    @Provides
    @IntoSet
    public StringSelectHandler provideNewsStringSelectCommand(NewsCommand newsCommand) {
        return newsCommand;
    }
}
