package com.travel.journal.trip;

import static android.Manifest.permission.INTERNET;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.room.Room;

import com.travel.journal.ApplicationController;
import com.travel.journal.R;
import com.travel.journal.room.Trip;
import com.travel.journal.room.TripDao;
import com.travel.journal.room.TripDataBase;
import com.travel.journal.weather.RetrofitUtil;
import com.travel.journal.weather.Weather;
import com.travel.journal.weather.WeatherApi;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Class used to show the trip.
 *
 * @author Alex_Smarandache
 */
public class ViewTripActivity extends AppCompatActivity implements LocationListener {

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
     * The last user location.
     */
    private String      lastLocation = "";

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
    public void onLocationChanged(Location location) {
        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            final List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                final String city = addresses.get(0).getLocality();
                final String country = addresses.get(0).getCountryName();
                final String newLocation = new StringBuilder(country).append(',').append(' ').append(city).toString();
                 if(!lastLocation.equals(newLocation)) {
                    lastLocation = newLocation;
                    updateCurrentLocationWeather(lastLocation);
                }
            }
        } catch (IOException e) {
            Log.e("GetLocation", e.getMessage(), e);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Not used
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Not used
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Not used
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_trip);

        final TripDataBase tripDataBase =
                Room.databaseBuilder(this, TripDataBase.class,
                        ApplicationController.TRIPS_DB_NAME).allowMainThreadQueries().build();

        final TripDao tripDao = tripDataBase.getTripDao();
        Bundle extras         = getIntent().getExtras();
        long tripId           = extras.getLong(ApplicationController.VIEW_TRIP_ID);
        trip                  = tripDao.getTrip(tripId);

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

        loadWeather();
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check if GPS is enabled
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS is not enabled, show a dialog to the user to enable it
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    /**
     * Check if the internet permission is given and request it to continue.
     */
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
     * Loads the weather for the current trip.
     */
    private void loadWeather() {
        final WeatherLoader weatherLoader = new WeatherLoader(this, trip.getDestination(), API_TOKEN, "metric");

        weatherLoader.registerListener(0, new Loader.OnLoadCompleteListener<Weather>() {
            @Override
            public void onLoadComplete(@NonNull Loader<Weather> loader, Weather data) {
                weatherLoader.unregisterListener(this);

                if (data != null) {
                    if (data.getCurrentTemperature() > 0) {
                        weatherInfo.icon.setImageResource(R.drawable.ic_outline_wb_sunny_24);
                        weatherInfo.icon.setColorFilter(ActivityCompat.getColor(ViewTripActivity.this, R.color.yellow_500));
                    } else {
                        weatherInfo.icon.setImageResource(R.drawable.ic_outline_ice_skating_24);
                        weatherInfo.icon.setColorFilter(ActivityCompat.getColor(ViewTripActivity.this, R.color.blue_500));
                    }

                    weatherInfo.currentTemperature.setText(String.format("%s%s", data.getCurrentTemperature(), getString(R.string.degree_symbol)));
                    weatherInfo.minimumTemperature.setText(String.format("%s%s", data.getMinTemperature(), getString(R.string.degree_symbol)));
                    weatherInfo.maximumTemperature.setText(String.format("%s%s", data.getMaxTemperature(), getString(R.string.degree_symbol)));
                    weatherInfo.wind.setText(String.format("%s %s", data.getWind(), getString(R.string.wind_unit)));
                    weatherInfo.cloud.setText(String.format("%s%s", data.getClouds(), getString(R.string.cloud_unit)));
                    weatherInfo.humidity.setText(String.format("%s%s", data.getHumidity(), getString(R.string.humidity_unit)));
                    weatherInfo.title.setText(R.string.the_weather_right_now);
                    weatherInfo.constraintLayout.setVisibility(View.VISIBLE);

                } else {
                    weatherInfo.title.setText(R.string.weather_not_available);
                }
            }
        });

        weatherLoader.startLoading();
    }

    /**
     * Loads the weather for the current trip.
     */
    private void updateCurrentLocationWeather(final String destination) {
        final WeatherLoader weatherLoader = new WeatherLoader(this, destination, API_TOKEN, "metric");

        weatherLoader.registerListener(0, new Loader.OnLoadCompleteListener<Weather>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onLoadComplete(@NonNull Loader<Weather> loader, Weather data) {
                weatherLoader.unregisterListener(this);

                if (data != null) {
                    weatherInfo.currentWeather.setText(String.format(getString(R.string.your_city_temp), data.getCurrentTemperature()));
                }
            }
        });

        weatherLoader.startLoading();
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
        weatherInfo.currentWeather = findViewById(R.id.current_weather);
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
         * The current weather.
         */
        private TextView currentWeather;

        /**
         * The icon.
         */
        private ImageView icon;

        /**
         * The constraint layout.
         */
        private ConstraintLayout constraintLayout;
    }

    /**
     * A custom AsyncTaskLoader that loads weather data for a given destination using a Retrofit API call.
     *
     * @author Alex_Smarandache
     */
    private static class WeatherLoader extends AsyncTaskLoader<Weather> {

        /**
         * The destination for which to fetch weather data.
         */
        private final String destination;

        /**
         * The API token to use for the weather service.
         */
        private final String apiToken;

        /**
         * The units to use for the weather data (e.g. metric or imperial).
         */
        private final String units;

        /**
         * Constructs a new WeatherLoader instance.
         * @param context The context in which to load the data.
         * @param destination The destination for which to fetch weather data.
         * @param apiToken The API token to use for the weather service.
         * @param units The units to use for the weather data (e.g. metric or imperial).
         */
        public WeatherLoader(Context context, String destination, String apiToken, String units) {
            super(context);
            this.destination = destination;
            this.apiToken = apiToken;
            this.units = units;
        }
        /**

         Loads the weather data for the given destination using a Retrofit API call.

         @return The weather data for the given destination, or null if an error occurs.
         */
        @Override
        public Weather loadInBackground() {
            Weather toReturn = null;
            final  WeatherApi service = RetrofitUtil.getRetrofitInstance().create(
                    WeatherApi.class);
            final Call<Weather> call = service.getWeather(destination, apiToken, units);

            try {
                final Response<Weather> response = call.execute();
                if (response.isSuccessful()) {
                    toReturn = response.body();
                }
            } catch (IOException e) {
                Log.e("LoadWeather", e.getMessage(), e);
            }

            return toReturn;
        }

        /**
         * Starts the loader, forcing it to load data.
         */
        @Override
        protected void onStartLoading() {
            forceLoad();
        }
    }
}