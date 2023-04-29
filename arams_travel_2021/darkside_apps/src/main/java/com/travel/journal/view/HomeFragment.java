package com.travel.journal.view;

import static android.content.Context.ALARM_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.travel.journal.ApplicationController;
import com.travel.journal.HomeActivity;
import com.travel.journal.R;
import com.travel.journal.notifications.NotificationHelper;
import com.travel.journal.trip.TripAdapter;
import com.travel.journal.room.Trip;
import com.travel.journal.room.TripDao;
import com.travel.journal.room.TripDataBase;

import java.util.List;

/**
 * Fragment for home section.
 *
 * @author Alex_Smarandache
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        HomeActivity.floatingActionButton.show();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TripDataBase tripDataBase
                = Room.databaseBuilder(view.getContext(), TripDataBase.class,
                ApplicationController.TRIPS_DB_NAME).allowMainThreadQueries().build();
        final TripDao tripDao
                = tripDataBase.getTripDao();
        final List<Trip> trips
                = tripDao.getUserTrips(ApplicationController.getLoggedInUser().getId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationHelper.notifyTripComingSoon(trips.get(0), view.getContext());
        }

        final View requiredView              = requireView();
        final RecyclerView recyclerViewTrips = requiredView.findViewById(R.id.recycler_view_trips);

        if(recyclerViewTrips != null) {
            View currentView = getView();
            if(currentView != null) {
                recyclerViewTrips.setLayoutManager(new LinearLayoutManager(currentView.getContext()));
            }
            recyclerViewTrips.setAdapter(new TripAdapter(trips));
        }

    }
}