package edu.northeastern.cs5500.starterbot.service;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.annotate.ExcludeClassFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageService;

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
    
    public BalanceSheetService provideBalanceSheetService(AlphaVantageService service) {
        return service;
    }
}
