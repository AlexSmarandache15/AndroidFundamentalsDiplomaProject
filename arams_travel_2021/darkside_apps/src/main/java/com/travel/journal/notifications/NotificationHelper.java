package com.travel.journal.notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.travel.journal.ApplicationController;
import com.travel.journal.R;
import com.travel.journal.room.Trip;
import com.travel.journal.trip.ViewTripActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class NotificationHelper {

    /**
     * The hidden constructor.
     */
    private NotificationHelper() {
        // nothing.
    }

    /**
     * Initialize the channel for notifications.
     */
    public static void initializeNotificationChannel(final NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ApplicationController.CHANNEL_ID,
                    ApplicationController.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Notify about a trip that will coming soon.
     *
     * @param trip     The trip that will comming soon.
     * @param context  The current context.
     */
    public static void notifyTripComingSoon(Trip trip, final Context context) {
        // Create an Intent to launch the notification
        final Intent intent = new Intent(context, ViewTripActivity.class);
        intent.putExtra(ApplicationController.VIEW_TRIP_ID, trip.getId());
        final PendingIntent pendingIntent =
                PendingIntent.getActivity(context, (int) trip.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ApplicationController.CHANNEL_ID)
                .setSmallIcon(R.drawable.traveler)
                .setContentTitle("Trip Reminder")
                .setContentText(String.format("Your trip to %s is coming up soon!", trip.getDestination()))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(new Random().nextInt(), builder.build());
    }

}
