package com.example.vikingesejllog.note;

import android.content.Context;

import im.delight.android.location.SimpleLocation;

public class MyNewGPS {

    private Context context;
    private SimpleLocation location;


    public MyNewGPS(Context context){
        this.context = context;
    }

    public SimpleLocation getLocation(){

        location = new SimpleLocation(context);

        if (!location.hasLocationEnabled())
            SimpleLocation.openSettings(context);

     return location;
    }

}