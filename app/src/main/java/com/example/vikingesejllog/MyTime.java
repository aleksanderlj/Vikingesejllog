package com.example.vikingesejllog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTime {


    public String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date());

    }
}