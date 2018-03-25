package com.utsavrajvir.firbaseauth;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by utsav on 3/22/2018.
 */

public class Contact {

    private double latitude;
    private double longitude;
    private String id;
    private String name;
    private String number;
    private String address;




    public Contact()
    {

    }


    public Contact(String id, String name, String number, String address, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
