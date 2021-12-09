package com.travel.journal.trip;

import static android.Manifest.permission.INTERNET;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.travel.journal.ApplicationController;
import com.travel.journal.R;
import com.travel.journal.weather.RetrofitUtil;
import com.travel.journal.weather.Weather;
import com.travel.journal.weather.WeatherApi;
import com.travel.journal.room.Trip;
import com.travel.journal.room.TripDao;
import com.travel.journal.room.TripDataBase;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class used to show the trip.
 *
 * @author Alex_Smarandache
 */
public class ViewTripActivity extends AppCompatActivity {

    /**
     * The trip DAO.
     */
    private TripDao     tripDao;

    /**
     * The current trip.
     */
    private Trip        trip;

    /**
     * The trip favorite button.
     */
    private ImageButton favoriteButton;

    /**
     * The trip picture.
     */
    private ImageView   tripPicture;

    /**
     * Used to store the information about weather.
     */
    private final LocalWeatherInformationStorage weatherInfo
            = new LocalWeatherInformationStorage();

    /**
     * Constant for API token.
     */
    private static final String API_TOKEN = "e1980f218fc410cc362331590255d98c";

    /**
     * Constant for permission to internet.
     */
    private static final int INTERNET_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_trip);

        TripDataBase tripDataBase = Room.databaseBuilder(this, TripDataBase.class, ApplicationController.TRIPS_DB_NAME).allowMainThreadQueries().build();

        tripDao       = tripDataBase.getTripDao();
        Bundle extras = getIntent().getExtras();
        long tripId   = extras.getLong(ApplicationController.VIEW_TRIP_ID);
        trip          = tripDao.getTrip(tripId);

        List<String> tripTypes = Arrays.asList(
                getString(R.string.city_break),
                getString(R.string.sea_side),
                getString(R.string.mountains)
        );

        initializeComponents(tripTypes);
        checkForInternetPermission();

        switch(trip.getType()){
            case TripConstants.CITY_BREAK:
                tripPicture.setImageResource(R.drawable.city_break_p);
                break;
            case TripConstants.SIDE_SEA:
                tripPicture.setImageResource(R.drawable.sea2);
                break;
            case TripConstants.MOUNTAINS:
                tripPicture.setImageResource(R.drawable.mountains);
                break;
            default:
                tripPicture.setImageResource(R.drawable.travel_photo);
        }

        if (trip.isFavorite()) {
            favoriteButton.setImageResource(R.drawable.ic_baseline_star_24);
        }

        WeatherApi service = RetrofitUtil.getRetrofitInstance().create(WeatherApi.class);
        Call<Weather> call = service.getWeather(trip.getDestination(), API_TOKEN, "metric");

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(@NonNull Call<Weather> call, @NonNull Response<Weather> response) {
                if(response.code() == 200) {
                    Weather main = response.body();
                    assert main != null;

                    if (main.getCurrentTemperature() > 0) {
                        weatherInfo.icon.setImageResource(R.drawable.ic_outline_wb_sunny_24);
                        weatherInfo.icon.setColorFilter(ActivityCompat.getColor(ViewTripActivity.this, R.color.yellow_500));
                    } else {
                        weatherInfo.icon.setImageResource(R.drawable.ic_outline_ice_skating_24);
                        weatherInfo.icon.setColorFilter(ActivityCompat.getColor(ViewTripActivity.this, R.color.blue_500));
                    }

                    weatherInfo.currentTemperature.setText(String.format("%s%s", main.getCurrentTemperature(), getString(R.string.degree_symbol)));
                    weatherInfo.minimumTemperature.setText(String.format("%s%s", main.getMinTemperature(), getString(R.string.degree_symbol)));
                    weatherInfo.maximumTemperature.setText(String.format("%s%s", main.getMaxTemperature(), getString(R.string.degree_symbol)));
                    weatherInfo.wind.setText(String.format("%s %s", main.getWind(), getString(R.string.wind_unit)));
                    weatherInfo.cloud.setText(String.format("%s%s", main.getClouds(), getString(R.string.cloud_unit)));
                    weatherInfo.humidity.setText(String.format("%s%s", main.getHumidity(), getString(R.string.humidity_unit)));
                    weatherInfo.title.setText(R.string.the_weather_right_now);
                    weatherInfo.constraintLayout.setVisibility(View.VISIBLE);

                } else {
                    weatherInfo.title.setText(R.string.weather_not_available);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Weather> call, @NonNull Throwable t) {
                weatherInfo.title.setText(R.string.weather_not_available);
            }

        });
    }

    private void checkForInternetPermission() {
        if (ContextCompat.checkSelfPermission(ViewTripActivity.this, INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewTripActivity.this, new String[]{INTERNET}, INTERNET_PERMISSION);
            Toast.makeText(ViewTripActivity.this, R.string.no_internet_permission, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == INTERNET_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ViewTripActivity.this, new String[]{INTERNET}, INTERNET_PERMISSION);
                Toast.makeText(ViewTripActivity.this, R.string.no_internet_permission, Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Has the responsibility to initialize all components.
     *
     * @param tripTypes The list with all types of trips.
     */
    private void initializeComponents(List<String> tripTypes) {
        TextView tripName           = findViewById(R.id.trip_name);
        TextView tripDestination    = findViewById(R.id.trip_destination);
        TextView tripPrice          = findViewById(R.id.trip_price);
        TextView tripType           = findViewById(R.id.trip_type);
        TextView tripStartDate      = findViewById(R.id.trip_start_date);
        TextView tripEndDate        = findViewById(R.id.trip_end_date);
        TextView tripRating         = findViewById(R.id.trip_rating);
        weatherInfo.currentTemperature = findViewById(R.id.weather_now_value);
        weatherInfo.minimumTemperature = findViewById(R.id.weather_min_value);
        weatherInfo.maximumTemperature = findViewById(R.id.weather_max_value);
        weatherInfo.wind = findViewById(R.id.weather_wind_value);
        weatherInfo.cloud = findViewById(R.id.weather_clouds_value);
        weatherInfo.humidity = findViewById(R.id.weather_humidity_value);
        weatherInfo.icon = findViewById(R.id.weather_icon);
        weatherInfo.constraintLayout = findViewById(R.id.weather_layout);
        weatherInfo.title = findViewById(R.id.weather_title);
        favoriteButton              = findViewById(R.id.button_favorite);
        tripPicture                 = findViewById(R.id.trip_picture);

        tripName.setText(trip.getName());
        tripDestination.setText(trip.getDestination());
        tripPrice.setText(String.valueOf(trip.getPrice()));
        tripType.setText(tripTypes.get(trip.getType()));
        tripStartDate.setText(trip.getStartDate());
        tripEndDate.setText(trip.getEndDate());
        tripRating.setText(String.valueOf(trip.getRating()));
    }

    /**
     * Inner class to store the weather details.
     *
     * @author Alex_Smarandache
     */
    private static class LocalWeatherInformationStorage {
        /**
         * The weather at this moment.
         */
        private TextView currentTemperature;

        /**
         * The minimum temperature.
         */
        private TextView minimumTemperature;

        /**
         * The maximum temperature.
         */
        private TextView maximumTemperature;

        /**
         * The wind.
         */
        private TextView wind;

        /**
         * The cloud.
         */
        private TextView cloud;

        /**
         * The humidity.
         */
        private TextView humidity;

        /**
         * The title.
         */
        private TextView title;

        /**
         * The icon.
         */
        private ImageView icon;

        /**
         * The constraint layout.
         */
        private ConstraintLayout constraintLayout;
    }
}