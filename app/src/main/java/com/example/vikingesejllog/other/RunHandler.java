package com.example.vikingesejllog.other;


import android.os.Handler;
import android.os.HandlerThread;

public class RunHandler {
    public static Handler get(){
        HandlerThread thread = new HandlerThread("thread");
        thread.start();
        //handler = new Handler(thread.getLooper());
        return new Handler();
    }
}
