package com.travel.journal.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Api for watcher interface.
 *
 * @author Alex_Smarandache
 */
public interface WeatherApi {

    @GET("/data/2.5/weather")
    Call<Weather> getWeather(@Query("q") String city, @Query("appid") String token, @Query("units") String units);

}
