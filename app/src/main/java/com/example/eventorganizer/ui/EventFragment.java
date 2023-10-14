package com.example.eventorganizer.ui;

import android.app.usage.UsageEvents;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventorganizer.R;
import com.example.eventorganizer.model.entities.Event;

public class EventFragment extends Fragment {

    View root;
    public static Event event;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event, container, false);
        TextView eventNameTextView = root.findViewById(R.id.event);
        eventNameTextView.setText(event.getName());
        return root;
    }
}