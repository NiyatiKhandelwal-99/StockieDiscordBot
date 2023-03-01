package edu.northeastern.cs5500.starterbot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PriceApi{
  public final AlphaVantageApiCall alphaVantageApiCall;

  public PriceApi(AlphaVantageApiCall apiCall){
    this.alphaVantageApiCall = apiCall;
  }

  public String getPrice(String ticker){
    StringBuilder queryUrl = createURL(ticker);

    String jsonOutput = alphaVantageApiCall.getRequest(queryUrl.toString());

    JsonElement element = JsonParser.parseString(jsonOutput);
    JsonObject priceObj = element.getAsJsonObject();

    // Invalid ticker passed
    if(!priceObj.get("Global Quote").getAsJsonObject().has("05. price")){return "";}

    LocalDate dateObj = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String date = dateObj.format(formatter);

    JsonObject globalQuoteObj = priceObj.get("Global Quote").getAsJsonObject();

    var lastTradingDate = globalQuoteObj.get("07. latest trading day").toString();

    if(lastTradingDate.replace("\"","").equals(date)){
      StringBuilder sb = new StringBuilder();
      return sb.append("After Market Price: ").append(globalQuoteObj.get("05. price").toString().replace("\"",""))
          .append("\tMarket Price: ")
          .append(globalQuoteObj.get("08. previous close")
              .toString().replace("\"","")).toString();
    }

    return globalQuoteObj.get("05. price").toString().replace("\"","");
  }

  private StringBuilder createURL(String ticker) {
    StringBuilder res = new StringBuilder();
    res.append("function="+Function.GLOBAL_QUOTE.name())
        .append("&symbol=").append(ticker).append("&apikey=");
    return res;
  }

}
