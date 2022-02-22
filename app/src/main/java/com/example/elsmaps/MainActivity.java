package com.example.elsmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    public ArrayList<Mission> missionList;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView xCoord,yCoord,missionName;
    private Button btnOpenNavigation, btnClosePopUp;
    FusedLocationProviderClient locationClient;
    SupportMapFragment mapFragment;
    private CameraPosition cameraPosition;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng defaultLocation = new LatLng(48.34536,14.16017);
    private static final int DEFAULT_ZOOM =15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;


    private Location lastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_maps);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        LatLng currentLocation;
        //Initialize fused location

        //check permission




        Mission mission1 = new Mission("Mission1", 48.34536,14.16017);
        Mission mission2 = new Mission("Mission2", 48.35485,14.16251);
        missionList = new ArrayList<Mission>();
        missionList.add(mission1);
        missionList.add(mission2);
        Mission mission3;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if(mMap!=null){
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }





    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setTrafficEnabled(true);
        for (Mission mission:missionList
             ) {
            mMap.addMarker(new MarkerOptions()
            .position(new LatLng(mission.X,mission.Y))
            .title(mission.Name)
            .snippet(String.valueOf(mission.X)))
                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mission.X,mission.Y)));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
            googleMap.setOnInfoWindowClickListener(this);
        }

//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(48.34536,14.16017))
//                .title("Marker in Sydney"))
//                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(48.34536,14.16017)));

        getLocationPermission();
        getDeviceLocation();
        updateLocationUI();



    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation(){
        try{
            if(locationPermissionGranted){
                Task<Location> locationResult= fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()){
                            lastKnownLocation = task.getResult();
                            if(lastKnownLocation!=null){
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                            else{

                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    private void getLocationPermission(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        createNewDialog(marker);

    }

    public void createNewDialog(Marker marker){
        dialogBuilder=new AlertDialog.Builder(this);
        final View missionPopUpView=getLayoutInflater().inflate(R.layout.popup,null);
        xCoord=(TextView) missionPopUpView.findViewById(R.id.popup_X);
        yCoord=(TextView) missionPopUpView.findViewById(R.id.popup_Y);
        missionName=(TextView) missionPopUpView.findViewById(R.id.popup_name);

        btnOpenNavigation=(Button) missionPopUpView.findViewById(R.id.popup_NavButton);
        btnClosePopUp=(Button) missionPopUpView.findViewById(R.id.popup_close);
        missionName.setText(marker.getTitle());
        LatLng markerPosition = marker.getPosition();
        xCoord.setText(String.valueOf(markerPosition.latitude));
        yCoord.setText(String.valueOf(markerPosition.longitude));
        dialogBuilder.setView(missionPopUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        btnOpenNavigation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startNavigation(markerPosition);
            }
        });
        btnClosePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
    public void startNavigation(LatLng markerPosition){
        String navigationString = markerPosition.latitude +","+ markerPosition.longitude;
        Uri gmmIntentUri= Uri.parse("google.navigation:q="+navigationString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }



}