package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.YahooFinanceException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.TopGainersService;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TopGainersController {

    TopGainersService topGainersService;

    @Inject
    TopGainersController(TopGainersService topGainersService) {
        this.topGainersService = topGainersService;
    }

    public Map<String, String> getTopGainers() throws RestException, YahooFinanceException {
        return topGainersService.getTopGainers();
    }
}
