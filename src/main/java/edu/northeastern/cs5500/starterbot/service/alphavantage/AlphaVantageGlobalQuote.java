package edu.northeastern.cs5500.starterbot.service.alphavantage;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AlphaVantageGlobalQuote {
    @SerializedName("01. symbol")
    private final String symbol;

    @SerializedName("02. open")
    private final String open;

    @SerializedName("03. high")
    private final String high;

    @SerializedName("04. low")
    private final String low;

    @SerializedName("05. price")
    private final String price;

    @SerializedName("06. volume")
    private final String volume;

    @SerializedName("07. latest trading day")
    private final String latestTradingDay;

    @SerializedName("08. previous close")
    private final String previousClose;

    @SerializedName("09. change")
    private final String change;

    @SerializedName("10. change percent")
    private final String changePercent;
}
