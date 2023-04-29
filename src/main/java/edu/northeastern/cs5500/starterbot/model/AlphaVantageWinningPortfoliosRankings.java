package edu.northeastern.cs5500.starterbot.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

@Data
public class AlphaVantageWinningPortfoliosRankings {
    @SerializedName("rank")
    private final String rank;

    @SerializedName("portfolio")
    private final List<AlphaVantageWinningPortfolios> portfolios;

    @SerializedName("measurement_start")
    private final String measurementStartDate;

    @SerializedName("measurement_end")
    private final String measurementEndDate;

    @SerializedName("percent_gain")
    private final String percentGain;

    @SerializedName("start_value_usd")
    private final String startValue;

    @SerializedName("end_value_usd")
    private final String endValue;
}
