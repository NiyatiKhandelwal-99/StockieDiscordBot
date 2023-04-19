package edu.northeastern.cs5500.starterbot.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AlphaVantageBalanceSheet {

    @SerializedName("totalAssets")
    private final String totalAssets;

    @SerializedName("fiscalDateEnding")
    private final String fiscalDateEnding;

    @SerializedName("investments")
    private final String investments;

    @SerializedName("totalLiabilities")
    private final String totalLiabilities;

    @SerializedName("currentDebt")
    private final String currentDebt;

    @SerializedName("reportedCurrency")
    private final String reportedCurrency;

    @SerializedName("totalCurrentAssets")
    private final String totalCurrentAssets;
}
