package com.example.eventorganizer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.eventorganizer.MainActivity;
import com.example.eventorganizer.R;
import com.example.eventorganizer.model.adapters.EventAdapter;
import com.example.eventorganizer.model.entities.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarFragment extends Fragment implements AdapterView.OnItemClickListener {
    private TextView noEventsTextView;
    private CalendarView calendarView;
    private EventAdapter eventAdapter;
    private ListView listView;
    private View root;
    private List<Event> events = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = root.findViewById(R.id.calendarView);
        noEventsTextView = root.findViewById(R.id.noEvents);
        listView = root.findViewById(R.id.listEventsByDate);
        eventAdapter = new EventAdapter(events, this);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        calendarView.setDate(calendar.getTimeInMillis(), true, true);


        showEventsForDate(year, month, dayOfMonth);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                showEventsForDate(year, month, dayOfMonth);
            }
        });
        listView.setOnItemClickListener(this);
        return root;
    }

    private void showEventsForDate(int year, int month, int dayOfMonth) {
        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
        events = MainActivity.dbHelper.getAllEventsByDate(selectedDate);

        if (events.size() > 0) {
            noEventsTextView.setVisibility(View.GONE);
        } else {
            noEventsTextView.setVisibility(View.VISIBLE);
        }
        eventAdapter.setData(events);
        listView.setAdapter(eventAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = events.get(i);
        EventFragment.event = event;
        Navigation.findNavController(root).navigate(R.id.action_nav_calendar_to_eventFragment);
    }
}
