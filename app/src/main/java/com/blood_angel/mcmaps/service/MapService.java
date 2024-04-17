package com.blood_angel.mcmaps.service;

import static android.graphics.Color.TRANSPARENT;
import static android.view.Gravity.BOTTOM;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.blood_angel.mcmaps.R;
import com.blood_angel.mcmaps.db.ImitationDB;
import com.blood_angel.mcmaps.domain.Place;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MapService implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private final Context context;
    private final String path = "gs://mymapsmcc.appspot.com";


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Toast.makeText(context, latLng.latitude + " " + latLng.longitude, Toast.LENGTH_SHORT).show();
    }

    public MapService(Context context) {
        this.context = context;
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        Toast.makeText(context, "Long " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        for (Place p :
                ImitationDB.getPlaces()) {
            googleMap.addMarker(new MarkerOptions().position(p.getLatLng()).title(p.getName()));
        }
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Place place = ImitationDB.getPlaceByName(marker.getTitle());
                BottomSheetDialog dialog = new BottomSheetDialog(context);

                dialog.setContentView(R.layout.dialog_fragment);
                dialog.getWindow().setGravity(BOTTOM);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
                dialog.getWindow().setLayout(WRAP_CONTENT, WRAP_CONTENT);
                dialog.show();

                ImageView image = dialog.getWindow().findViewById(R.id.iv_image);
                TextView tv_name = dialog.getWindow().findViewById(R.id.tv_name);
                TextView tv_address = dialog.getWindow().findViewById(R.id.tv_address);
                TextView tv_description = dialog.getWindow().findViewById(R.id.tv_description);
                tv_address.setText(place.getAddress());
                tv_name.setText(place.getName());
                tv_description.setText(place.getInformation());
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance(path);
                StorageReference reference = firebaseStorage.getReference(place.getPathToImage());
                Glide.with(context).load(reference).into(image);
                return false;
            }
        });
    }
}
