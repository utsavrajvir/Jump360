package com.utsavrajvir.firbaseauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by utsav on 3/22/2018.
 */

//This class is a type of Adapter class

public class ContactList extends ArrayAdapter<Contact> {

    private Activity context;
    private List<Contact> contactlsit; //Variable to store data into ArrayList



    public ContactList(Activity context, List<Contact> contactlsit)
    {
        super(context, R.layout.activity_profile,contactlsit);
        this.context = context;
        this.contactlsit = contactlsit;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View listviewItem = inflater.inflate(R.layout.activity_contact_list,null,true);

        final TextView name = (TextView) listviewItem.findViewById(R.id.textViewName);
        final TextView number = (TextView) listviewItem.findViewById(R.id.textViewNumber);
        final TextView address = (TextView) listviewItem.findViewById(R.id.textViewAddress);


        Contact contact = contactlsit.get(position);

        //Set Text
        name.setText(contact.getName());
        number.setText(contact.getNumber());

        if(contact.getAddress()!=null)
            address.setText(contact.getAddress());


        return listviewItem;
    }
}
