package com.travel.journal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.travel.journal.room.User;
import com.travel.journal.sign_in.LoginActivity;

/**
 * The main activity in application.
 *
 * @author Alex_Smarandache
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_TravelJournal);

        final Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ApplicationController.setLoggedInUser(gson.fromJson(preferences.getString(ApplicationController.LOGGED_IN_USER, ""), User.class), this);

        startActivity(ApplicationController.getLoggedInUser() != null ?
                new Intent(this, HomeActivity.class) :
                new Intent(this, LoginActivity.class));

        finish();
    }
}