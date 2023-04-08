package edu.northeastern.cs5500.starterbot.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

@Data
public class AlphaVantageNewsResponse {
    @SerializedName("items")
    private final String items;

    @SerializedName("sentiment_score_definition")
    private final String sentimentScoreDefinition;

    @SerializedName("relevance_score_definition")
    private final String relevanceScoreDefinition;

    @SerializedName("feed")
    private final List<AlphaVantageNewsFeed> feed;
}
