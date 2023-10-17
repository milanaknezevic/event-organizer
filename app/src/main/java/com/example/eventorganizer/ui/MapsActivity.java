package com.example.eventorganizer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.eventorganizer.R;
import com.example.eventorganizer.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String selectedLocationName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34.0, 151.0);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Location").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMarkerDragListener(this);
    }

    public void saveLocationButton(View view) {
        Intent intent = new Intent();
        intent.putExtra("selectedLocationName", selectedLocationName);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (isNetworkAvailable(this)) {
            Geocoder geocoder = new Geocoder(this);
            LatLng position = marker.getPosition();
            try {
                List<Address> addressList = geocoder.getFromLocation(position.latitude, position.longitude, 1);

                if (addressList != null && !addressList.isEmpty()) {
                    Address address = addressList.get(0);
                    String locationName = address.getAddressLine(0);
                    selectedLocationName = locationName;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        android.net.NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }
}
