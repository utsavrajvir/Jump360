package com.utsavrajvir.firbaseauth;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class ProfileActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;
    ListView ContactListView;

    DatabaseReference databaseReference;
    List<Contact> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        this.setTitle("Contact");

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();


        if(!user.isEmailVerified())
        {
            //Code to display alert dialog if user have not verify his email and get back to login page
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle("You not have Email Verified");
            alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                }
            });

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

        }




        databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());

        contactList = new ArrayList<>();

        ContactListView = (ListView)findViewById(R.id.listView1);


    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }


        databaseReference.addValueEventListener(new ValueEventListener() {

            //Code to fetch data into ArrayList every time new record gets Add
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                contactList.clear();
                for(DataSnapshot contactSnapshot : dataSnapshot.getChildren())
                {

                    Contact contact = contactSnapshot.getValue(Contact.class);
                    contactList.add(contact);
                }
                ContactList adapter = new ContactList(ProfileActivity.this,contactList);
                ContactListView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


 /* View.OnClickListener,  @Override
    public void onClick(View v) {

        startActivity(new Intent(getApplicationContext(),AddContact.class));
    }
*/
    public void onClick(View view)
    {
        startActivity(new Intent(getApplicationContext(),AddContact.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //code to logout. This is beacuse it gives every EventSource to get Handle from class file itself.
        switch(item.getItemId())
        {
            case R.id.menu1:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }



        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void mapOpen(View view) {
        Intent i = new Intent(this,Map.class);
        startActivity(i);
    }
}
