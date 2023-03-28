package edu.northeastern.cs5500.starterbot.service;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageService;

@Module
public class ServiceModule {
    @Provides
    public QuoteService provideQuoteService(AlphaVantageService service) {
        return service;
    }
}
