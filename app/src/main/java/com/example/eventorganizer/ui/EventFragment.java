package com.example.eventorganizer.ui;

import android.app.usage.UsageEvents;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventorganizer.R;
import com.example.eventorganizer.model.entities.Event;

public class EventFragment extends Fragment {

    View root;


    public static Event event;
  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }
}