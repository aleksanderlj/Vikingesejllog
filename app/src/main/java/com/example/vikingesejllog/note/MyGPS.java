package com.example.vikingesejllog.note;

import android.content.Context;

import im.delight.android.location.SimpleLocation;

public class MyGPS {

    private Context context;


    public MyGPS(Context context){
        this.context = context;
    }

    public SimpleLocation getLocation(){

        SimpleLocation location = new SimpleLocation(context);

        if (!location.hasLocationEnabled())
            SimpleLocation.openSettings(context);

     return location;
    }

}