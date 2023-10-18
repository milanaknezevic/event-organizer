package com.example.eventorganizer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventorganizer.MainActivity;
import com.example.eventorganizer.R;
import com.example.eventorganizer.model.adapters.EventAdapter;
import com.example.eventorganizer.model.entities.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class EventsFragment extends Fragment implements AdapterView.OnItemClickListener {

    View root;
    List<Event> events = new ArrayList<>();
    ListView listView;
    EventAdapter eventAdapter;
    SearchView searchView;
    FloatingActionButton addEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        root = inflater.inflate(R.layout.fragment_events, container, false);
        listView = root.findViewById(R.id.listEvents);
        addEvent=root.findViewById(R.id.addButton);
        searchView = root.findViewById(R.id.searchView);
        //MainActivity.dbHelper.deleteEvents();
        events = MainActivity.dbHelper.getAllEvents();
        eventAdapter = new EventAdapter(events, this);
        listView.setAdapter(eventAdapter);


        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.addEventFragment);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Event> e = MainActivity.dbHelper.searchEventsByName(s);
                eventAdapter.setData(e);
                return true;
            }
        });
        listView.setOnItemClickListener(this);


        return root;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = events.get(i);
        EventFragment.event = event;
        Navigation.findNavController(root).navigate(R.id.action_nav_events_to_eventFragment);
    }
}