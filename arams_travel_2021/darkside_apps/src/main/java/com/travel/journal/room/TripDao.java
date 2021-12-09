package com.travel.journal.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * The trip DAO.
 *
 * @author Alex_Smarandache
 */
@Dao
public interface TripDao {

    /**
     * Query to get trip by id.
     *
     * @param tripId The trip id.
     *
     * @return The founded trip.
     */
    @Query(QueriesUtil.GET_TRIP_BY_ID)
    Trip getTrip(long tripId);


    /**
     * Get all trips of given user.
     *
     * @param userId The id of the user.
     *
     * @return A list with all trips founded.
     */
    @Query(QueriesUtil.GET_TRIP_BY_USER)
    List<Trip> getUserTrips(long userId);


    /**
     * Performs trip insertion operation.
     *
     * @param trip The trip to insert.
     *
     */
    @Insert
    void insert(Trip trip);


    /**
     * Performs update trip operation. In conflict case, the trip will be replaced.
     *
     * @param trip The trip to update.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Trip trip);


    /**
     * Performs the add trip to favorite list operation.
     *
     * @param tripId The trip to be added in favorites list.
     */
    @Query(QueriesUtil.SET_TRIP_FAVORITE)
    void setFavorite(long tripId);


    /**
     * Performs the remove trip to favorite list operation.
     *
     * @param tripId The trip to be removed in favorites list.
     */
    @Query(QueriesUtil.UNSET_TRIP_FAVORITE)
    void removeFavorite(long tripId);


    /**
     * Performs operation to delete trip by its id.
     *
     * @param tripId The id of trip that must be deleted.
     */
    @Query(QueriesUtil.DELETE_TRIP_BY_ID)
    void delete(long tripId);
}