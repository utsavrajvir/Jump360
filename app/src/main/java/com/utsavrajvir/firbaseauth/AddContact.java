package com.utsavrajvir.firbaseauth;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

public class AddContact extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;


    EditText editTextName,editTextNumber,editTextAddress; //Variables for fetching EditText Values


    DatabaseReference databaseContact; //Database Connectivity Variable
    FirebaseAuth mAuth; //Fetch Current User

    FirebaseUser user;//Variable to get Data of Current User Logged in

    private LatLng lat;//Lattitude

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        this.setTitle("AddContacts");


        editTextName = (EditText)findViewById(R.id.name);
        editTextNumber = (EditText)findViewById(R.id.number);
        editTextAddress = (EditText)findViewById(R.id.address);
       // findViewById(R.id.contact).setOnClickListener(this);

// Getting Database Instance and Getting Current User
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        databaseContact = FirebaseDatabase.getInstance().getReference(user.getUid());

        //Initializing google Client
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

    }


    //Set data such as latitude and longitude
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                lat = place.getLatLng();
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                editTextAddress.setText(place.getAddress());
            }
        }
    }


    //To open place picker activity
    public void map(View view)
    {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddContact.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    //to connect GoogleAppClient Connecting on Start
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public void onclick(View v) {


        addContact();
    }

    private void addContact()
    {
        String name = editTextName.getText().toString(),number = editTextNumber.getText().toString();
        String address = editTextAddress.getText().toString();



        if(name.isEmpty())
        {
            editTextName.setError("Name is Required");
            editTextName.requestFocus();
            return;
        }


        if(!Patterns.PHONE.matcher(number).matches())
        {
            editTextNumber.setError("Please Enter Valid Number");
            editTextNumber.requestFocus();
            return;
        }

        if(address.isEmpty())
        {
            editTextAddress.setError("Please Select Address by Clicking TextView");
            editTextAddress.requestFocus();
            return;
        }

        String id = databaseContact.push().getKey();
        Contact contact = new Contact(id,name,number,address,lat.latitude,lat.longitude);
        databaseContact.child(id).setValue(contact);
        Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(this, "Could'nt Connect", Toast.LENGTH_SHORT).show();

    }
}
