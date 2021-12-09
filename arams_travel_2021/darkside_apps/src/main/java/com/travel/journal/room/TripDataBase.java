package com.travel.journal.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Database for trips.
 *
 * @author Alex_Smarandache
 */
@Database(entities = {Trip.class}, version = 1, exportSchema = false)
public abstract class TripDataBase extends RoomDatabase {

    /**
     * @return The current trip Dao.
     */
    public abstract TripDao getTripDao();
}