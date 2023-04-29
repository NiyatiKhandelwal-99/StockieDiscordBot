package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.YahooFinanceException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.TopGainersService;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/** TopGainersController is responsible for calling the service to perform the business logic. */
@Singleton
public class TopGainersController {

    TopGainersService topGainersService;

    @Inject
    TopGainersController(TopGainersService topGainersService) {
        this.topGainersService = topGainersService;
    }

    /**
     * getTopGainers trsansfer control to the service to perform the business logic. There is event
     * verification required for this method since there are no parameters passed by the user.
     *
     * @return Map<String, String>
     * @throws RestException
     * @throws YahooFinanceException
     */
    public Map<String, String> getTopGainers() throws RestException, YahooFinanceException {
        return topGainersService.getTopGainers();
    }
}
