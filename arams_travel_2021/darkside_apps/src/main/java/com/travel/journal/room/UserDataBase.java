package com.travel.journal.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Database for users.
 *
 * @author Alex_Smarandache
 */
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDataBase extends RoomDatabase {
    public abstract UserDao getUserDao();
}