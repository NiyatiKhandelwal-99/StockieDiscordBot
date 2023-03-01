package edu.northeastern.cs5500.starterbot.service.alphavantage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AlphaVantageProperties {
    private static Properties propFile;

    static {
        InputStream is =
                AlphaVantageProperties.class.getResourceAsStream("/alphaVantage.properties");
        propFile = new Properties();
        try {
            propFile.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return propFile.getProperty(key);
    }
}
