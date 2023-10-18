package com.example.eventorganizer.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventorganizer.R;


public class CalendarFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.fragment_calendar, container, false);

       /* root.findViewById(R.id.button_home).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_events);
            }
        });*/

        return root;
    }
}