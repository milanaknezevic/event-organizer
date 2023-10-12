package com.example.eventorganizer.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.eventorganizer.MainActivity;
import com.example.eventorganizer.R;
import com.example.eventorganizer.model.adapters.EventAdapter;
import com.example.eventorganizer.model.entities.Event;

import java.util.ArrayList;
import java.util.List;


public class EventsFragment  extends Fragment implements AdapterView.OnItemClickListener {

    View root;
    List<Event> events=new ArrayList<>();
    ListView listView;
    EventAdapter eventAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        root=inflater.inflate(R.layout.fragment_events, container, false);
        listView=root.findViewById(R.id.listEvents);
        events= MainActivity.dbHelper.getAllEvents();
        eventAdapter=new EventAdapter(events,this);
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(this);
        return root;
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event event=events.get(i);
       EventFragment.event=event;
        Navigation.findNavController(root).navigate(R.id.action_nav_events_to_eventFragment);
    }
}