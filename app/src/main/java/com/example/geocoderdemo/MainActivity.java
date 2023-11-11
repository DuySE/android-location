package com.example.geocoderdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private TextView txtViewLocation;
    private Location userLocation;

    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtViewLocation = findViewById(R.id.txtViewLocation);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.getDefault());
        askPermission();
        userLocation = new Location(LocationManager.GPS_PROVIDER);
        Button button = findViewById(R.id.btnGetDistance);
        button.setOnClickListener(view -> calculateDistance());
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            int LOCATION_REQUEST_CODE = 1;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    private void calculateDistance() {
        String locationName = "330 E Columbia St, New Westminster, BC, Canada";
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            double endLatitude = addressList.get(0).getLatitude();
            double endLongitude = addressList.get(0).getLongitude();
            float[] results = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), endLatitude, endLongitude, results);
            String result = getString(R.string.distance, results[0]);
            Log.d("AAA", result);
            txtViewLocation.setGravity(View.TEXT_ALIGNMENT_CENTER);
            txtViewLocation.setText(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        userLocation.setLatitude(location.getLatitude());
        userLocation.setLongitude(location.getLongitude());
    }
}