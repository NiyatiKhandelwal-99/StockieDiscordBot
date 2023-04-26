package edu.northeastern.cs5500.starterbot.service;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.annotate.ExcludeClassFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageService;
import edu.northeastern.cs5500.starterbot.service.yahoofinance.YahooFinanceService;

@Module
@ExcludeClassFromGeneratedCoverage
public class ServiceModule {

    @Provides
    public QuoteService provideQuoteService(AlphaVantageService service) {
        return service;
    }

    @Provides
    public NewsFeedService provideNewsFeedService(AlphaVantageService service) {
        return service;
    }

    @Provides
    public IncomeStatementService provideIncomeStatementService(AlphaVantageService service) {
        return service;
    }

    @Provides
    public BalanceSheetService provideBalanceSheetService(AlphaVantageService service) {
        return service;
    }

    @Provides
    public TopGainersService provideTopGainersService(YahooFinanceService service) {
        return service;
    }

    @Provides
    public TopLosersService provideTopLosersService(YahooFinanceService service) {
        return service;
    }
}
