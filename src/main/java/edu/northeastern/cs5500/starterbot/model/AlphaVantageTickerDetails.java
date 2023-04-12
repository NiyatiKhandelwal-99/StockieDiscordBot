package edu.northeastern.cs5500.starterbot.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AlphaVantageTickerDetails {
    @SerializedName("symbol")
    private final String symbol;

    @SerializedName("name")
    private final String name;

    @SerializedName("matchScore")
    private final String matchScore;
}
