package com.utsavrajvir.firbaseauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    List<Contact> contactList;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        this.setTitle("Contact Location");

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());

        contactList = new ArrayList<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setUpMap();
        addMarker();
    }

    private void addMarker() {
        if(map!=null && contactList.size()>0){
          for(Contact c : contactList){
              Log.v("MapMarker",c.getName());
              if(c.getLatitude()==0 && c.getLongitude()==0)
                  continue;
              LatLng banglore = new LatLng(c.getLatitude(),c.getLongitude());
              Marker bang = map.addMarker(new MarkerOptions().position(banglore).title(c.getName()).snippet(c.getNumber()));
              map.moveCamera(CameraUpdateFactory.newLatLngZoom(banglore, 15));
              map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

          }
        }
    }


    public void setUpMap() {

        //map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactList.clear();
                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {

                    Contact contact = contactSnapshot.getValue(Contact.class);
                    contactList.add(contact);
                }
                addMarker();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}