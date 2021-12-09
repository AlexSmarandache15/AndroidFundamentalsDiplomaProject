package com.travel.journal.weather;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit util methods inclusive for the singleton instance.
 *
 * @author Alex_Smarandache
 */
public class RetrofitUtil {

    /**
     * Hidden constructor.
     */
    private RetrofitUtil() {
        // empty constructor
    }

    /**
     * Helper class to manage the singleton instance.
     *
     * @author Alex_Smarandache
     */
    private static class RetrofitHelper {
        static final String BASE_URL = "https://api.openweathermap.org/";
        static final Retrofit RETROFIT_INSTANCE = new retrofit2.Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    /**
     * Get the retrofit instance.
     *
     * @return The instance.
     */
    public static Retrofit getRetrofitInstance() {
        return RetrofitHelper.RETROFIT_INSTANCE;
    }
}
