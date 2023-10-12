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

    private List<Event> events=new ArrayList<>();
    LayoutInflater layoutInflater;
    Fragment fragment;


    public EventAdapter(List<Event> events,Fragment fragment)
    {
        this.events=events;
        this.fragment=fragment;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(layoutInflater==null)
        {
            layoutInflater=fragment.getLayoutInflater();
        }

        if(view==null)
        {
           view=layoutInflater.inflate(R.layout.list_item_element,null);
        }

       TextView name = view.findViewById(R.id.eventItemList);
       name.setText(events.get(i).getName());
        return view;
    }
}
