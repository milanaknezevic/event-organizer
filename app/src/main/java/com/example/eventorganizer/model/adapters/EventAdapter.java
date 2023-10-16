package com.example.eventorganizer.model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.eventorganizer.R;
import com.example.eventorganizer.model.entities.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends BaseAdapter {

    private List<Event> events = new ArrayList<>();
    LayoutInflater layoutInflater;
    Fragment fragment;

    public EventAdapter(List<Event> events, Fragment fragment) {
        this.events = events;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setData(List<Event> events) {
        this.events.clear();
        this.events.addAll(events);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater == null) {
            layoutInflater = fragment.getLayoutInflater();
        }

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item_element, null);
        }

        Event event = events.get(i);


        TextView title = view.findViewById(R.id.event_name);
        TextView datetime = view.findViewById(R.id.event_time);
        TextView location = view.findViewById(R.id.event_location);
        TextView category = view.findViewById(R.id.event_category);


        title.setText(event.getName());
        datetime.setText(event.getTime());
        location.setText(event.getLocation());

        category.setText(String.valueOf(event.getCategory()));

        return view;
    }
}
