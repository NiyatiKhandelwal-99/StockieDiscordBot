package edu.northeastern.cs5500.starterbot.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AlphaVantageGlobalQuoteResponse {
    @SerializedName("Global Quote")
    private final AlphaVantageGlobalQuote globalQuote;
}
