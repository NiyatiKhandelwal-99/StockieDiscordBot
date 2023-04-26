package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.YahooFinanceException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.TopLosersService;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TopLosersController {

    TopLosersService topLosersService;

    @Inject
    TopLosersController(TopLosersService topLosersService) {
        this.topLosersService = topLosersService;
    }

    public Map<String, String> getTopLosers() throws RestException, YahooFinanceException {
        return topLosersService.getTopLosers();
    }
}
