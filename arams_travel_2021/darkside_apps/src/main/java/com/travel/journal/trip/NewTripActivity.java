package com.travel.journal.trip;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.room.Room;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.travel.journal.ApplicationController;
import com.travel.journal.HomeActivity;
import com.travel.journal.R;
import com.travel.journal.room.Trip;
import com.travel.journal.room.TripDao;
import com.travel.journal.room.TripDataBase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Used to show the add a new trip view.
 *
 * @author Alex_Smarandache
 */
public class NewTripActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    /**
     * List with all trips types.
     */
    private List<String> tripTypes;

    /**
     * The trip DAO.
     */
    private TripDao tripDao;

    /**
     * The extras.
     */
    private Bundle extras;

    /**
     * Button to delete the trip.
     */
    private Button deleteButton;

    /**
     * Used to store all trip information.
     */
    private final TripInformationStorage tripInfo = new TripInformationStorage();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        extras = getIntent().getExtras();

        Objects.requireNonNull(this.getSupportActionBar()).setTitle(extras == null ?
                R.string.add_trip_title : R.string.edit_trip);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.add_new_trip);

        tripTypes                 = Arrays.asList(getString(R.string.city_break), getString(R.string.sea_side), getString(R.string.mountains));
        TripDataBase tripDataBase = Room.databaseBuilder(this, TripDataBase.class, ApplicationController.TRIPS_DB_NAME).allowMainThreadQueries().build();
        tripDao                   = tripDataBase.getTripDao();

        initializeComponents();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, tripTypes);
        tripInfo.type.setAdapter(spinnerAdapter);
        tripInfo.type.setOnItemSelectedListener(this);

        if (extras != null) {
            long editableTripId = extras.getLong(ApplicationController.EDIT_TRIP_ID);
            tripInfo.trip = tripDao.getTrip(editableTripId);

            tripInfo.name.setText(tripInfo.trip.getName());
            tripInfo.destination.setText(tripInfo.trip.getDestination());
            tripInfo.type.setText(tripTypes.get(tripInfo.trip.getType()), false);
            tripInfo.startDate.setText(tripInfo.trip.getStartDate());
            tripInfo.endDate.setText(tripInfo.trip.getEndDate());
            tripInfo.price.setValue((float) tripInfo.trip.getPrice());
            tripInfo.rating.setRating((float) tripInfo.trip.getRating());

            deleteButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Responsible to initialize all components.
     */
    private void initializeComponents() {
        tripInfo.name              = findViewById(R.id.text_field_trip_name_value);
        tripInfo.destination       = findViewById(R.id.text_field_destination_value);
        tripInfo.type              = findViewById(R.id.text_field_trip_type_value);
        tripInfo.startDate         = findViewById(R.id.text_field_start_date_value);
        tripInfo.endDate           = findViewById(R.id.text_field_end_date_value);
        tripInfo.price             = findViewById(R.id.slider_price);
        tripInfo.rating            = findViewById(R.id.rating_bar);

        deleteButton               = findViewById(R.id.button_delete_trip);

        tripInfo.nameLayout        = findViewById(R.id.text_field_trip_name);
        tripInfo.destinationLayout = findViewById(R.id.text_field_destination);
        tripInfo.startDateLayout   = findViewById(R.id.text_field_start_date);
        tripInfo.endDateLayout     = findViewById(R.id.text_field_end_date);
        tripInfo.typeLayout        = findViewById(R.id.dropdown_trip_type);

        tripInfo.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!tripInfo.name.getText().toString().isEmpty()) tripInfo.nameLayout.setError(null);
                else tripInfo.nameLayout.setError(getString(R.string.error_required));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        });

        tripInfo.destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tripInfo.destinationLayout.setError(!tripInfo.destination.getText().toString().isEmpty()
                ? null : getString(R.string.error_required));
            }

            @Override
            public void afterTextChanged(Editable s) {
               // not needed
            }
        });
    }

    
    /**
     * Used to set date.
     *
     * @param field The string field.
     */
    private void setDateField(String field) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(TripConstants.DATE_PATTERN, Locale.ROOT);

        tripInfo.datePicker = new DatePickerDialog(this, R.style.my_dialog_theme,
                (view, year, month, dayOfMonth) -> {
            Calendar pickedDate = Calendar.getInstance();
            pickedDate.set(year, month, dayOfMonth);

            final String formattedDate = dateFormat.format(pickedDate.getTime());

            if (TripConstants.START_DATE.equals(field)) {
                tripInfo.startDate.setText(formattedDate);
            } else if (TripConstants.END_DATE.equals(field)) {
                tripInfo.endDate.setText(formattedDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        initializeDatePickerValidation(field, dateFormat);
    }


    /**
     * Used to initialize the data picker validation.
     *
     * @param field      The String field
     * @param dateFormat The data format
     */
    private void initializeDatePickerValidation(String field, SimpleDateFormat dateFormat) {
        tripInfo.datePicker.getDatePicker().setFirstDayOfWeek(2);

        if (field.equals("startDate")) {
            if (!tripInfo.endDate.getText().toString().isEmpty()) {
                try {
                    String tripEndDateValue = tripInfo.endDate.getText().toString();
                    tripInfo.datePicker.getDatePicker().setMaxDate(Objects.requireNonNull(dateFormat.parse(tripEndDateValue)).getTime());
                } catch (ParseException e) {
                    Toast.makeText(this, getString(R.string.generic_exception_text), Toast.LENGTH_LONG).show();
                }
            }
        } else if (field.equals("endDate")) {
            if (!tripInfo.startDate.getText().toString().isEmpty()) {
                try {
                    String tripStartDateValue = tripInfo.startDate.getText().toString();
                    tripInfo.datePicker.getDatePicker().setMinDate(Objects.requireNonNull(dateFormat.parse(tripStartDateValue)).getTime());
                } catch (ParseException e) {
                    Toast.makeText(this, getString(R.string.generic_exception_text), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Opens the start date picker.
     *
     * @param view The view.
     */
    public void openStartDatePicker(View view) {
        setDateField("startDate");
        tripInfo.datePicker.show();
    }


    /**
     * Opens the end date picker.
     *
     * @param view The view.
     */
    public void openEndDatePicker(View view) {
        setDateField("endDate");
        tripInfo.datePicker.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedTripType = tripTypes.get(position);
        Toast.makeText(parent.getContext(), selectedTripType, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // not needed
    }


    /**
     * Used to save the trip in database.
     *
     * @param view The view.
     */
    public void saveTrip(View view) {
        if (!isValid()) {
            long newTripUserId        = ApplicationController.getLoggedInUser().getId();
            String newTripName        = tripInfo.name.getText().toString().trim();
            String newTripDestination = tripInfo.destination.getText().toString().trim();
            int newTripType           = tripTypes.indexOf(tripInfo.type.getText().toString().trim());
            double newTripPrice       = tripInfo.price.getValue();
            String newTripStartDate   = tripInfo.startDate.getText().toString().trim();
            String newTripEndDate     = tripInfo.endDate.getText().toString().trim();
            double newTripRating      = tripInfo.rating.getRating();

            Trip newTrip = new Trip(newTripUserId, newTripName, newTripDestination, newTripType, newTripPrice, newTripStartDate, newTripEndDate, newTripRating);

            if (extras != null) {
                newTrip.setId(tripInfo.trip.getId());
                tripDao.update(newTrip);
            } else {
                tripDao.insert(newTrip);
            }

            startActivity(new Intent(view.getContext(), HomeActivity.class));
            finish();
        }
    }


    /**
     * @return <code>true</code> if the trip is valid.
     */
    private boolean isValid() {
        boolean wereErrors = false;

        if (tripInfo.name.getText().toString().isEmpty()) {
            tripInfo.nameLayout.setError(getString(R.string.error_required));
            wereErrors = true;
        }

        if (tripInfo.destination.getText().toString().isEmpty()) {
            tripInfo.destinationLayout.setError(getString(R.string.error_required));
            wereErrors = true;
        }

        if (tripInfo.type.getText().toString().isEmpty()) {
            tripInfo.typeLayout.setError(getString(R.string.error_required));
            wereErrors = true;
        } else {
            tripInfo.typeLayout.setError(null);
        }

        if (tripInfo.startDate.getText().toString().isEmpty()) {
            tripInfo.startDateLayout.setError(getString(R.string.error_required));
            wereErrors = true;
        } else {
            tripInfo.startDateLayout.setError(null);
        }

        if (tripInfo.endDate.getText().toString().isEmpty()) {
            tripInfo.endDateLayout.setError(getString(R.string.error_required));
            wereErrors = true;
        } else {
            tripInfo.endDateLayout.setError(null);
        }

        return wereErrors;
    }


    /**
     * Method used to delete the trip.
     *
     * @param view The view.
     */
    public void deleteTrip(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogTheme);

        builder.setTitle(R.string.trip_delete_title).setMessage(R.string.trip_delete_description);

        builder.setPositiveButton(R.string.yes_delete, (di, i) -> {
            tripDao.delete(tripInfo.trip.getId());
            Intent intent = new Intent(view.getContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        });

        builder.setNeutralButton(R.string.cancel, (di, i) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));
    }


    /**
     * Used to store the information about the trip.
     *
     * @author Alex_Smarandache
     */
    private static class TripInformationStorage {

        /**
         * The trip name layout.
         */
        TextInputLayout nameLayout;

        /**
         * The trip destination layout.
         */
        TextInputLayout destinationLayout;

        /**
         * The trip start date layout.
         */
        TextInputLayout startDateLayout;

        /**
         * The trip end date layout.
         */
        TextInputLayout endDateLayout;

        /**
         * The trip type layout.
         */
        TextInputLayout typeLayout;

        /**
         * The trip name.
         */
        EditText name;

        /**
         * The trip destination.
         */
        EditText destination;

        /**
         * The trip start date.
         */
        EditText startDate;

        /**
         * The trip end date.
         */
        EditText endDate;

        /**
         * The trip type.
         */
        AutoCompleteTextView type;

        /**
         * The trip price.
         */
        Slider price;

        /**
         * The trip rating bar.
         */
        RatingBar rating;

        /**
         * The trip date picker.
         */
        DatePickerDialog datePicker;

        /**
         * The trip that is edit.
         */
         Trip trip;

    }
}