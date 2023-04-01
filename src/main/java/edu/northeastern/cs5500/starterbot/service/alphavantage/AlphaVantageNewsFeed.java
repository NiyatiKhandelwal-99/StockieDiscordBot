package edu.northeastern.cs5500.starterbot.service.alphavantage;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

@Data
public class AlphaVantageNewsFeed {
    @SerializedName("title")
    private final String title;

    @SerializedName("url")
    private final String url;

    @SerializedName("time_published")
    private final String timePublished;

    @SerializedName("authors")
    private final String[] authors;

    @SerializedName("summary")
    private final String summary;

    @SerializedName("banner_image")
    private final String bannerImage;

    @SerializedName("source")
    private final String source;

    @SerializedName("category_within_source")
    private final String categoryWithinSource;

    @SerializedName("source_domain")
    private final String sourceDomain;

    @SerializedName("overall_sentiment_score")
    private final String overallSentimentScore;

    @SerializedName("overall_sentiment_label")
    private final String overallSentimentLabel;

    @SerializedName("topics")
    private final List<AlphaVantageNewsTopic> topics;
}
