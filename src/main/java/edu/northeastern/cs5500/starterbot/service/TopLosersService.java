package edu.northeastern.cs5500.starterbot.service;

import edu.northeastern.cs5500.starterbot.exception.YahooFinanceException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import java.util.Map;

public interface TopLosersService extends Service {

    Map<String, String> getTopLosers() throws RestException, YahooFinanceException;
}
