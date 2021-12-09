package com.travel.journal.weather;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.util.Map;

/**
 * Class for weather representation.
 *
 * @author Alex_Smarandache
 */
public class Weather {
    /**
     * The weather main.
     */
    @Expose
    private final Map<String, Double> main;

    /**
     * The weather clouds.
     */
    @Expose
    private final Map<String, Double> clouds;

    /**
     * The weather wind.
     */
    @Expose
    private final Map<String, Double> wind;



    /**
     * Constructor.
     *
     * @param main    The weather main.
     * @param wind    The weather wind.
     * @param clouds  The weather clouds.
     */
    public Weather(Map<String, Double> main,
                   Map<String, Double> wind,
                   Map<String, Double> clouds) {
        this.main = main;
        this.wind = wind;
        this.clouds = clouds;
    }

    /**
     * @return The current temperature.
     */
    public int getCurrentTemperature() {
        Double temperature =  main.get(WeatherConstants.TEMP);
        return temperature != null ? (int) Math.ceil(temperature) : 0;
    }

    /**
     * @return The current minimum temperature.
     */
    public int getMinTemperature() {
        Double minTemperature = main.get(WeatherConstants.MIN_TEMP);
        return minTemperature != null ? (int) Math.ceil(minTemperature) : 0;
    }

    /**
     * @return The current maximum temperature.
     */
    public int getMaxTemperature() {
        Double maxTemperature = main.get(WeatherConstants.MAX_TEMP);
        return maxTemperature != null ? (int) Math.ceil(maxTemperature) : 0;
    }

    /**
     * @return The humidity.
     */
    public int getHumidity() {
        Double humidity = main.get(WeatherConstants.HUMIDITY);
        return humidity != null ? (int) Math.ceil(humidity) : 0;
    }

    /**
     * @return The speed.
     */
    public int getWind() {
        Double windSpeed = wind.get(WeatherConstants.SPEED);
        return windSpeed != null ? (int) Math.ceil(windSpeed) : 0;
    }

    /**
     * @return The all clouds.
     */
    public int getClouds() {
        Double cloudsInfo = clouds.get(WeatherConstants.ALL);
        return cloudsInfo != null ? (int) Math.ceil(cloudsInfo) : 0;
    }


    @NonNull
    @Override
    public String toString() {
        return "Weather{" +
                "main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                '}';
    }

}
