package edu.northeastern.cs5500.starterbot.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

@Data
public class AlphaVantageWinningPortfoliosResponse {

    @SerializedName("season")
    private final String season;

    @SerializedName("ranking")
    private final List<AlphaVantageWinningPortfoliosRankings> rankings;
}
