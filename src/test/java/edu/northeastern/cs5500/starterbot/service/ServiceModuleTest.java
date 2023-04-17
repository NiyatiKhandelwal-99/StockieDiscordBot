package edu.northeastern.cs5500.starterbot.service;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageService;
import org.junit.jupiter.api.Test;

public class ServiceModuleTest {
    @Test
    public void testProvideQuoteService() {
        assertThat(new ServiceModule().provideQuoteService(new AlphaVantageService())).isNotNull();
    }

    @Test
    public void testProvideNewsFeedService() {
        assertThat(new ServiceModule().provideNewsFeedService(new AlphaVantageService()))
                .isNotNull();
    }
}
