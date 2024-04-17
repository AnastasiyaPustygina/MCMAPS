package com.blood_angel.mcmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.blood_angel.mcmaps.service.MapService;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(new MapService(this));
    }
}