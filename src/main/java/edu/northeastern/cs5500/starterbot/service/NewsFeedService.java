package edu.northeastern.cs5500.starterbot.service;

import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.alphavantage.AlphaVantageNewsFeed;

public interface NewsFeedService extends Service {

    AlphaVantageNewsFeed[] getNewsSentiment(String symbol, String fromTime) throws RestException;
}
