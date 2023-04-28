package edu.northeastern.cs5500.starterbot.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

@Data
public class AlphaVantageTickerResponse {
    @SerializedName("feed")
    private final List<AlphaVantageTickerDetails> tickerDetails;
}
