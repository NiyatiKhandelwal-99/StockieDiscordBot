package edu.northeastern.cs5500.starterbot.service.alphavantage;

import com.google.gson.Gson;
import edu.northeastern.cs5500.starterbot.exception.rest.BadRequestException;
import edu.northeastern.cs5500.starterbot.exception.rest.InternalServerErrorException;
import edu.northeastern.cs5500.starterbot.exception.rest.NotFoundException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.NewsFeedService;
import edu.northeastern.cs5500.starterbot.service.QuoteService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class AlphaVantageService implements QuoteService, NewsFeedService {
    private static final String BASE_URL = "https://www.alphavantage.co/query?";
    private final String apiKey;
    private static final String LIMITS_EXCEEDED =
            "{    \"Note\": \"Thank you for using Alpha Vantage! Our standard API call frequency is 5 calls per minute and 500 calls per day. Please visit https://www.alphavantage.co/premium/ if you would like to target a higher API call frequency.\"}";

    public AlphaVantageService(String alphaVantageApiKey) {
        this.apiKey = alphaVantageApiKey;
    }

    @Override
    public void register() {
        log.info("AlphaVantageService > register");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("ALPHA_VANTAGE_API_KEY is required");
        }
    }

    @Inject
    public AlphaVantageService() {
        this(new ProcessBuilder().environment().get("ALPHA_VANTAGE_API_KEY"));
    }

    @Override
    @SneakyThrows({InterruptedException.class})
    public AlphaVantageGlobalQuote getQuote(String symbol)
            throws RestException, AlphaVantageException {
        String queryUrl = "function=GLOBAL_QUOTE&symbol=" + symbol;
        String response = getRequest(queryUrl);

        long backoff = 1;

        while (LIMITS_EXCEEDED.equals(response)) {
            backoff *= 2;
            log.info("API limit exceeded; waiting {} seconds and trying again", backoff);
            if (backoff > 64) {
                throw new AlphaVantageException("Limit exceeded");
            }
            Thread.sleep(backoff * 1000);
            response = getRequest(queryUrl);
        }

        Gson gson = new Gson();
        AlphaVantageGlobalQuote quote =
                gson.fromJson(response, AlphaVantageGlobalQuoteResponse.class).getGlobalQuote();
        if (quote.getSymbol() == null) {
            throw new NotFoundException();
        }
        return quote;
    }

    @SneakyThrows({MalformedURLException.class, IOException.class})
    private String getRequest(String queryUrl) throws RestException {
        StringBuilder val = new StringBuilder();
        URL url = new URL(BASE_URL + queryUrl + "&apikey=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        switch (conn.getResponseCode()) {
            case HttpURLConnection.HTTP_OK:
                break;
            case HttpURLConnection.HTTP_BAD_REQUEST:
                throw new BadRequestException();
            case HttpURLConnection.HTTP_NOT_FOUND:
                throw new NotFoundException();
            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                throw new InternalServerErrorException();
            default:
                throw new RestException("unknown", conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        while ((output = br.readLine()) != null) {
            val.append(output);
        }
        conn.disconnect();

        return val.toString();
    }

    @Override
    public AlphaVantageNewsFeed[] getNewsSentiment(String symbol, String fromTime)
            throws RestException {
        String queryUrl = "function=NEWS_SENTIMENT&tickers=" + symbol + "&time_from=" + fromTime;
        String response = getRequest(queryUrl);

        System.out.println("response length = " + response.length());

        Gson gson = new Gson();
        var newsFeed = gson.fromJson(response, AlphaVantageNewsResponse.class).getFeed();
        if (newsFeed == null) {
            // This means the given symbol was not valid and no data was returned
            return null;
        }
        return newsFeed;
    }
}
