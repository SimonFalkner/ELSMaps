package com.example.elsmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    private Button btnOpenNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Mission mission1 = new Mission("Mission1", 48.34536,14.16017);
        Mission mission2 = new Mission("Mission2", 48.35485,14.16251);
        missionList = new ArrayList<Mission>();
        missionList.add(mission1);
        missionList.add(mission2);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


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
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();

        createNewDialog(marker);

    }


    public void createNewDialog(Marker marker){
        dialogBuilder=new AlertDialog.Builder(this);
        final View missionPopUpView=getLayoutInflater().inflate(R.layout.popup,null);
        xCoord=(TextView) missionPopUpView.findViewById(R.id.popup_X);
        yCoord=(TextView) missionPopUpView.findViewById(R.id.popup_Y);
        missionName=(TextView) missionPopUpView.findViewById(R.id.popup_name);

        btnOpenNavigation=(Button) missionPopUpView.findViewById(R.id.popup_NavButton);

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


    }
    public void startNavigation(LatLng markerPosition){
        String navigationString = markerPosition.latitude +","+ markerPosition.longitude;
        Uri gmmIntentUri= Uri.parse("google.navigation:q="+navigationString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}