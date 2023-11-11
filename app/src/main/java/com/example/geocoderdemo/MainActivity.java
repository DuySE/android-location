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
    private Location gpsLocation;

    private final int LOCATION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtViewLocation = findViewById(R.id.txtViewLocation);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        askPermission();
        calculateDistance();
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
        gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    private void calculateDistance() {
        String locationName = "330 E Columbia St, New Westminster, BC";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            double endLatitude = addressList.get(0).getLatitude();
            double endLongitude = addressList.get(0).getLongitude();
            float[] results = new float[1];
            Location.distanceBetween(gpsLocation.getLatitude(), gpsLocation.getLongitude(), endLatitude, endLongitude, results);
            txtViewLocation.setText(results[0] + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }
}