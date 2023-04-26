package edu.northeastern.cs5500.starterbot.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

@Data
public class AlphaVantageIncomeStatementResponse {
    @SerializedName("symbol")
    private final String symbol;

    @SerializedName("annualReports")
    private final List<AlphaVantageIncomeStatement> feed;
}
