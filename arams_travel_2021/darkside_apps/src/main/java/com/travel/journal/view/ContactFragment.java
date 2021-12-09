package com.travel.journal.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.travel.journal.HomeActivity;
import com.travel.journal.R;

/**
 * Fragment for Contact section.
 *
 * @author Alex_Smarandache
 */
public class ContactFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_contact, container, false);

        HomeActivity.floatingActionButton.hide();

        return root;
    }
}