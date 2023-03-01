package edu.northeastern.cs5500.starterbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class AlphaVantageApiCall implements ApiCall {
  private static final String BASE_URL = "https://www.alphavantage.co/query?";
  private final String apiKey;
  private final String functionName;

  public AlphaVantageApiCall(String functionName) {
    this.apiKey = AlphaVantageProperties.getProperty("apikey");
    this.functionName = functionName;
  }

  @Override
  public String getRequest(String queryUrl) {

    StringBuilder val = new StringBuilder();
    try {
      URL url = new URL(BASE_URL+queryUrl+apiKey);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");

      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
      }

      BufferedReader br = new BufferedReader(new InputStreamReader(
          (conn.getInputStream())));

      String output;
      while ((output = br.readLine()) != null) {
        val.append(output);
      }
      conn.disconnect();

    } catch (Exception ignored) {
    }
    return val.toString();
  }
}
