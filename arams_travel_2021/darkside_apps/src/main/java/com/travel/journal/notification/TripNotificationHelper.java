package com.travel.journal.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.travel.journal.ApplicationController;
import com.travel.journal.R;
import com.travel.journal.room.Trip;
import com.travel.journal.trip.ViewTripActivity;

import java.time.LocalDate;
import java.time.Month;

public class TripNotificationHelper {

    private static final String TRIP_NOTIFICATION_CHANNEL_ID = "TRIP_NOTIFICATION_CHANNEL_ID";

    public static void createNotification(Context context, Trip trip) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, TRIP_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Trip Reminder")
                .setContentText(getNotificationText(trip))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getPendingIntent(context, trip))
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) trip.getId() * 5, builder.build());
    }

    private static String getNotificationText(Trip trip) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            final String[] dateInfo = trip.getStartDate().split("/");
            LocalDate startDate = LocalDate.of(Integer.parseInt(dateInfo[2]), Month.of(Integer.parseInt(dateInfo[1])),
                    Integer.parseInt(dateInfo[0]));
            LocalDate now = LocalDate.now();

            return "Your trip to " + trip.getDestination() + " is starting in 3 days. Click to view details.";

//            if (ChronoUnit.HOURS.between(LocalDateTime.parse(trip.getStartDate() + "T00:00"), LocalDateTime.now()) <= 72) {
//                return "Your trip to " + trip.getDestination() + " is starting in 3 days. Click to view details.";
//            } else {
//                return "";
//            }
        }

        return "";

    }

    private static PendingIntent getPendingIntent(Context context, Trip trip) {
        Intent intent = new Intent(context, ViewTripActivity.class);
        intent.putExtra(ApplicationController.VIEW_TRIP_ID, trip.getId());
        return PendingIntent.getActivity(context, (int) trip.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(TRIP_NOTIFICATION_CHANNEL_ID, "Trip Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
