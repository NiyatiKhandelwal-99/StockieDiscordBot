package edu.northeastern.cs5500.starterbot.service;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageApi;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageService;

@Module
public class ServiceModule {
    @Provides
    public AlphaVantageApi provideAlphaVantageApi(AlphaVantageService service) {
        return service;
    }
}
