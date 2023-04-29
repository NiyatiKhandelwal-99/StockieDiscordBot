package edu.northeastern.cs5500.starterbot.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AlphaVantageWinningPortfolios {

    @SerializedName("symbol")
    private final String symbol;

    @SerializedName("shares")
    private final String shares;
}
