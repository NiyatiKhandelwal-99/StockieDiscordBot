package edu.northeastern.cs5500.starterbot.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AlphaVantageIncomeStatement {

    @SerializedName("fiscalDateEnding")
    private final String fiscalDateEnding;

    @SerializedName("reportedCurrency")
    private final String reportedCurrency;

    @SerializedName("totalRevenue")
    private final String totalRevenue;

    @SerializedName("costOfRevenue")
    private final String costOfRevenue;

    @SerializedName("grossProfit")
    private final String grossProfit;

    @SerializedName("netIncome")
    private final String netIncome;

    @SerializedName("operatingIncome")
    private final String operatingIncome;

    @SerializedName("researchAndDevelopment")
    private final String researchAndDevelopment;

    @SerializedName("interestIncome")
    private final String interestIncome;

    @SerializedName("interestExpense")
    private final String interestExpense;

    @SerializedName("incomeBeforeTax")
    private final String incomeBeforeTax;

    @SerializedName("incomeTaxExpense")
    private final String incomeTaxExpense;
}
