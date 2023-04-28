package edu.northeastern.cs5500.starterbot.service.yahoofinance;

import edu.northeastern.cs5500.starterbot.annotate.ExcludeClassFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.exception.YahooFinanceException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.TopGainersService;
import edu.northeastern.cs5500.starterbot.service.TopLosersService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * YahooFinanceService is responsible for handling the api calls to yahoo finance service
 * and scraping the details on the web page and transform them to a response model.
 */
@Singleton
@Slf4j
@ExcludeClassFromGeneratedCoverage
public class YahooFinanceService implements TopGainersService, TopLosersService {

    private static final String BASE_URL = "https://finance.yahoo.com";
    private static final String GAINERS_URL = "/gainers";
    private static final String LOSERS_URL = "/losers";
    private static final String TABLE_ROW = "tr";
    private static final String TABLE_COLUMN = "td";
    private static final int TABLE_SYMBOL_INDEX = 0;
    private static final int TABLE_PRICE_CHANGE_INDEX = 3;

    @Inject
    YahooFinanceService() {}

    @Override
    public void register() {
        log.info("YahooFinanceService > register");
    }

    /**
     * getTopGainers function is responsible for constructing the URL 
     * for gainers command call to the Yahoo Service. 
     * This function calls the mapping function to map the response to the model class.
     *
     * @return Map<String, String>
     * @throws RestException
     * @throws YahooFinanceException
     */
    @Override
    public Map<String, String> getTopGainers() throws RestException, YahooFinanceException {

        String gainersUrl = BASE_URL + GAINERS_URL;
        Map<String, String> gainers = fetchTickerPriceChangeMappings(gainersUrl);

        return sortByPriceChangeFactor(gainers, false);
    }

    /**
     * getTopLosers function is responsible for constructing the URL 
     * for losers command call to the Yahoo Service. 
     * This function calls the mapping function to map the response to the model class.
     *
     * @return Map<String, String>
     * @throws RestException
     * @throws YahooFinanceException
     */
    @Override
    public Map<String, String> getTopLosers() throws RestException, YahooFinanceException {
        String losersUrl = BASE_URL + LOSERS_URL;
        Map<String, String> losers = fetchTickerPriceChangeMappings(losersUrl);

        return sortByPriceChangeFactor(losers, true);
    }

    /**
     * fetchTickerPriceChangeMappings function is responsible for deconstructing the response
     * and mapping it into ticker and their coreesponding price changes.
     *
     * @return Map<String, String>
     * @throws RestException
     * @throws YahooFinanceException
     */
    public Map<String, String> fetchTickerPriceChangeMappings(String url) {
        Map<String, String> tickerPriceChangeMapping = new HashMap<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table[class=\"W(100%)\"]").first();
            Elements rows = table.select(TABLE_ROW);

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select(TABLE_COLUMN);

                String symbol = cols.get(TABLE_SYMBOL_INDEX).text();
                String priceChange = cols.get(TABLE_PRICE_CHANGE_INDEX).text();
                tickerPriceChangeMapping.put(symbol, priceChange);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tickerPriceChangeMapping;
    }

     /**
      * sortByPriceChangeFactor fubnction is responsible for sorting the ticker and price change mappings 
      * based on the price change value. The function also takes in a parameter to decide if the mappings 
      * need to be sorted in either an ascen ding or descending fashion based on the 
      * gainers or losers command being called.

      * @param tickerPriceChangeMapping
      * @param isAscending
      * @return Map<String, String> : sorted mapping of ticker and price changes based on price
      */
    public Map<String, String> sortByPriceChangeFactor(
            Map<String, String> tickerPriceChangeMapping, boolean isAscending) {
        List<Map.Entry<String, String>> list = new ArrayList<>(tickerPriceChangeMapping.entrySet());

        Collections.sort(
                list,
                new Comparator<Map.Entry<String, String>>() {
                    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                        double v1 = Double.parseDouble(o1.getValue());
                        double v2 = Double.parseDouble(o2.getValue());
                        return Double.compare(v1, v2) * (isAscending ? 1 : -1);
                    }
                });

        Map<String, String> sortedTickerPriceChangeMapping = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : list) {
            sortedTickerPriceChangeMapping.put(entry.getKey(), entry.getValue());
        }

        return sortedTickerPriceChangeMapping;
    }
}
