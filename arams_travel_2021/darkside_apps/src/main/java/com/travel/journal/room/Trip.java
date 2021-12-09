package com.travel.journal.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Class for trip representation.
 *
 * @author Alex_Smarandache
 */
@Entity
public class Trip implements Serializable {

    /**
     * Trip ID.
     */
    @PrimaryKey(autoGenerate = true)
    private long id;

    /**
     * The id for user.
     */
    @ColumnInfo(name = "user_id", index = true)
    private final long userId;

    /**
     * The name of the trip.
     */
    private String name;

    /**
     * The destination of the trip.
     */
    private final String destination;

    /**
     * Trip Type.
     */
    private final int type;

    /**
     * The trip price.
     */
    private final double price;

    /**
     * The start date of the trip.
     */
    @ColumnInfo(name = "start_date")
    private final String startDate;

    /**
     * The end date of the trip.
     */
    @ColumnInfo(name = "end_date")
    private final String endDate;

    /**
     * The rating of the trip.
     */
    private final double rating;

    /**
     * <code>true</code> if trip is in the favorites list.
     */
    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;


    /**
     * Constructor.
     *
     * @param userId        The user id.
     * @param name          The trip name.
     * @param destination   The trip destination.
     * @param type          The trip type.
     * @param price         The trip price.
     * @param startDate     The trip start date.
     * @param endDate       The trip end date.
     * @param rating        The trip rating.
     */
    public Trip(long userId, String name, String destination, int type, double price, String startDate, String endDate, double rating) {
        this.userId = userId;
        this.name = name;
        this.destination = destination;
        this.type = type;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
    }

    /**
     * @return The trip ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Update the trip ID.
     *
     * @param id The new trip id.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The user id of the trip.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Update the trip name.
     *
     * @param name The new trip name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The trip name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The trip destination.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @return The trip type.
     */
    public int getType() {
        return type;
    }

    /**
     * @return The trip price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return The trip start date.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @return The trip end date.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @return The trip rating.
     */
    public double getRating() {
        return rating;
    }

    /**
     * Update the favorite status fot the current trip.
     *
     * @param favorite <code>true</code> if the trip is favorite.
     */
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    /**
     * @return <code>true</code> if the trip is favorite.
     */
    public boolean isFavorite() {
        return isFavorite;
    }

    @NotNull
    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", user_id=" + userId +
                ", name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", rating=" + rating +
                '}';
    }
}
