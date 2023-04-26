package edu.northeastern.cs5500.starterbot.service.yahoofinance;

import edu.northeastern.cs5500.starterbot.annotate.ExcludeClassFromGeneratedCoverage;
import edu.northeastern.cs5500.starterbot.exception.YahooFinanceException;
import edu.northeastern.cs5500.starterbot.exception.rest.RestException;
import edu.northeastern.cs5500.starterbot.service.TopGainersService;
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

@Singleton
@Slf4j
@ExcludeClassFromGeneratedCoverage
public class YahooFinanceService implements TopGainersService {

    private static final String BASE_URL = "https://finance.yahoo.com";
    private static final String GAINERS_URL = "/gainers";
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

    @Override
    public Map<String, String> getTopGainers() throws RestException, YahooFinanceException {

        Map<String, String> gainers = new HashMap<>();
        String gainersUrl = BASE_URL + GAINERS_URL;
        try {
            Document doc = Jsoup.connect(gainersUrl).get();
            Element table = doc.select("table[class=\"W(100%)\"]").first();
            Elements rows = table.select(TABLE_ROW);

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select(TABLE_COLUMN);

                String symbol = cols.get(TABLE_SYMBOL_INDEX).text();
                String priceChange = cols.get(TABLE_PRICE_CHANGE_INDEX).text();
                gainers.put(symbol, priceChange);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sortByPriceChangeFactor(gainers);
    }

    public Map<String, String> sortByPriceChangeFactor(Map<String, String> gainers) {
        List<Map.Entry<String, String>> list = new ArrayList<>(gainers.entrySet());

        Collections.sort(
                list,
                new Comparator<Map.Entry<String, String>>() {
                    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                        double v1 = Double.parseDouble(o1.getValue());
                        double v2 = Double.parseDouble(o2.getValue());
                        return Double.compare(v1, v2) * -1;
                    }
                });

        Map<String, String> sortedGainers = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : list) {
            sortedGainers.put(entry.getKey(), entry.getValue());
        }

        return sortedGainers;
    }
}
