package edu.northeastern.cs5500.starterbot.service.alphavantage;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AlphaVantageNewsTopic {
    @SerializedName("topic")
    private final String topic;
}
