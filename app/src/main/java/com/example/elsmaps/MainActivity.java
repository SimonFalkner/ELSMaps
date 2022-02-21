package com.example.elsmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

        Mission mission1 = new Mission("Mission1", 48.8,14.5);
        Mission mission2 = new Mission("Mission2", 47.9,14.2);
        missionList = new ArrayList<Mission>();
        missionList.add(mission1);
        missionList.add(mission2);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        for (Mission mission:missionList
             ) {
            String snippedString=mission.X+"\n"+mission.Y;
            mMap.addMarker(new MarkerOptions()
            .position(new LatLng(mission.X,mission.Y))
            .title(mission.Name)
            .snippet(snippedString))
                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mission.X,mission.Y)));
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

    }


    public void createNewDialog(){
        dialogBuilder=new AlertDialog.Builder(this);
        final View missionPopUpView=getLayoutInflater().inflate(R.layout.popup,null);
        xCoord=(TextView) missionPopUpView.findViewById(R.id.popup_X);
        yCoord=(TextView) missionPopUpView.findViewById(R.id.popup_Y);
        missionName=(TextView) missionPopUpView.findViewById(R.id.popup_name);
        btnOpenNavigation=(Button) missionPopUpView.findViewById(R.id.pupup_NavButton);
    }
}