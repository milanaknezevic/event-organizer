package com.example.eventorganizer.ui;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.eventorganizer.R;
import com.example.eventorganizer.enums.Category;
import com.example.eventorganizer.model.entities.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class EventFragment extends Fragment {

    View root;
    public static Event event;
    private GoogleMap googleMap;
    private MapView mapView;
    private TextView eventNameTextView;
    private TextView eventCategoryTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private String location;
    private Category category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event, container, false);
        eventNameTextView = root.findViewById(R.id.event_name);
        eventNameTextView.setText(event.getName());
        eventCategoryTextView = root.findViewById(R.id.event_category_name);
        eventCategoryTextView.setText(String.valueOf(event.getCategory()));
        descriptionTextView = root.findViewById(R.id.event_description);
        descriptionTextView.setText(event.getDescription());
        dateTextView = root.findViewById(R.id.date_time_event);
        dateTextView.setText(event.getTime());
        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        location = event.getLocation();
        category=event.getCategory();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;

                if (location != null) {
                    try {
                        showLocationOnMap(location);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        return root;
    }

    private void showLocationOnMap(String location) throws IOException {

        Geocoder geocoder = new Geocoder(getActivity());
        double latitude = 0.0;
        double longitude = 0.0;
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                latitude = address.getLatitude();
                longitude = address.getLongitude();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        LatLng locationLatLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15));


        googleMap.addMarker(new MarkerOptions()
                .position(locationLatLng)
                .title("Lokacija")
                .snippet(location));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}