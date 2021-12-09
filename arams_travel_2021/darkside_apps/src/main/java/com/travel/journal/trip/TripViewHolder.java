package com.travel.journal.trip;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.travel.journal.ApplicationController;
import com.travel.journal.R;
import com.travel.journal.room.TripDao;
import com.travel.journal.room.TripDataBase;

/**
 * Class for trip view holder.
 *
 * @author Alex_Smarandache
 *
 */
public class TripViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    /**
     * The name of the trip.
     */
    private final TextView name;

    /**
     * The destination of the trip.
     */
    private final TextView destination;

    /**
     * The favorite button.
     */
    private final ImageButton favoriteButton;

    /**
     * The picture of the trip.
     */
    private final ImageView picture;

    /**
     * The trip DAO.
     */
    private final TripDao tripDao;

    /**
     * The trip id.
     */
    private long id;

    /**
     * <code>true</code> if the trip is favorite.
     */
    private boolean isFavorite;


    /**
     * Constructor.
     *
     * @param itemView The trip item view.
     */
    public TripViewHolder(@NonNull View itemView) {
        super(itemView);

        TripDataBase tripDataBase         = Room.databaseBuilder(itemView.getContext(),
                TripDataBase.class, ApplicationController.TRIPS_DB_NAME).allowMainThreadQueries().build();
        MaterialCardView materialCardView = itemView.findViewById(R.id.card);

        tripDao        = tripDataBase.getTripDao();
        name           = itemView.findViewById(R.id.trip_name);
        destination    = itemView.findViewById(R.id.trip_destination);
        favoriteButton = itemView.findViewById(R.id.button_favorite);
        picture        = itemView.findViewById(R.id.trip_picture);

        materialCardView.setOnClickListener(this);
        materialCardView.setOnLongClickListener(this);

        favoriteButton.setOnClickListener(v -> {
            if (!isFavorite) {
                tripDao.setFavorite(id);
                setFavorite(true);
                favoriteButton.setImageResource(R.drawable.ic_baseline_star_24);
                Snackbar.make(v, v.getContext().getString(R.string.favorites_added), BaseTransientBottomBar.LENGTH_SHORT).show();
            } else {
                tripDao.removeFavorite(id);
                setFavorite(false);
                favoriteButton.setImageResource(R.drawable.ic_baseline_star_outline_24);
                Snackbar.make(v, v.getContext().getString(R.string.favorites_removed), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @return The trip id.
     */
    public long getId() {
        return id;
    }

    /**
     * Update the trip id with the given argument.
     *
     * @param id The new id of the trip.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Update the trip favorite status.
     *
     * @param favorite <code>true</code> if the trip is favorite.
     */
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    /**
     * @return The trip name.
     */
    public TextView getName() {
        return name;
    }

    /**
     * @return The trip destination.
     */
    public TextView getDestination() {
        return destination;
    }

    /**
     * @return The favorite button fot this trip item.
     */
    public ImageButton getFavoriteButton() {
        return favoriteButton;
    }

    /**
     * @return The trip picture.
     */
    public ImageView getPicture() {
        return picture;
    }


    /**
     * Opens the view only trip.
     *
     * @param v The view.
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ViewTripActivity.class);
        intent.putExtra(ApplicationController.VIEW_TRIP_ID, this.getId());
        v.getContext().startActivity(intent);
    }


    /**
     * Opens the trip to edit.
     *
     * @param v The view.
     *
     * @return <code>true</code> if no errors are founded.
     */
    @Override
    public boolean onLongClick(View v) {
        Intent intent = new Intent(v.getContext(), NewTripActivity.class);
        intent.putExtra(ApplicationController.EDIT_TRIP_ID, this.getId());
        v.getContext().startActivity(intent);

        return true;
    }
}