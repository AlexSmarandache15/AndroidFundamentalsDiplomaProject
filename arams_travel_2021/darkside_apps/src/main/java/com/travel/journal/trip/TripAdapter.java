package com.travel.journal.trip;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.journal.R;
import com.travel.journal.room.Trip;

import java.util.List;

/**
 * The trip adapter.
 *
 * @author  Alex_Smarandache
 *
 */
public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {

    /**
     * The trips list.
     */
    private final List<Trip> trips;


    /**
     * Constructor.
     *
     * @param trips The trips list.
     */
    public TripAdapter(List<Trip> trips) {
        this.trips = trips;
    }


    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TripViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip currentTrip = trips.get(position);
        holder.setId(currentTrip.getId());

        if (currentTrip.isFavorite()) {
            holder.getFavoriteButton().setImageResource(R.drawable.ic_baseline_star_24);
        }

        switch(currentTrip.getType()) {
            case TripConstants.CITY_BREAK:
                holder.getPicture().setImageResource(R.drawable.city_break_p);
                break;
            case TripConstants.SIDE_SEA:
                holder.getPicture().setImageResource(R.drawable.sea2);
                break;
            case TripConstants.MOUNTAINS:
                holder.getPicture().setImageResource(R.drawable.mountains);
                break;
            default:
                holder.getPicture().setImageResource(R.drawable.travel_photo);
        }

        holder.setFavorite(currentTrip.isFavorite());
        holder.getName().setText(currentTrip.getName());
        holder.getDestination().setText(currentTrip.getDestination());
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }
}
