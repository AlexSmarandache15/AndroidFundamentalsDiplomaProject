package com.travel.journal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.travel.journal.room.User;

/**
 * Application to get the global data to control to entire application.
 *
 * @author Alex_Smaranache
 */
public abstract class ApplicationController {

    /**
     * The channel Id for notifications.
     */
    public final static String CHANNEL_ID = "arams.travel.notif.chanel";

    /**
     * The channel name for notifications.
     */
    public final static String CHANNEL_NAME = "AramsTravel";

    /**
     * Constant for logged in user.
     */
    public final static String LOGGED_IN_USER = "LOGGED_IN_USER";

    /**
     * Constant for edit trip id.
     */
    public final static String EDIT_TRIP_ID   = "EDIT_TRIP_ID";

    /**
     * Constant for view trip id.
     */
    public final static String VIEW_TRIP_ID   = "VIEW_TRIP_ID";

    /**
     * Constant for users database name.
     */
    public final static String USERS_DB_NAME  = "users.db";

    /**
     * Constant for trips database name.
     */
    public final static String TRIPS_DB_NAME  = "trips.db";

    /**
     * The current logged user.
     */
    private static User loggedInUser;

    /**
     * @return The logged user.
     */
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Used to set the new logged user.
     *
     * @param loggedInUser The user to log in.
     * @param context      The context.
     */
    public static void setLoggedInUser(User loggedInUser, Context context) {
        final SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        final Gson gson                       = new Gson();

        if (loggedInUser != null) {
            String userJson = gson.toJson(loggedInUser);
            editor.putString(ApplicationController.LOGGED_IN_USER, userJson);
        } else {
            editor.putString(ApplicationController.LOGGED_IN_USER, null);
        }

        editor.apply();
        ApplicationController.loggedInUser = loggedInUser == null ? null :
                gson.fromJson(preferences.getString(ApplicationController.LOGGED_IN_USER, ""), User.class);

    }
}
