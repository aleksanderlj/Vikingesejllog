package com.example.vikingesejllog.other;

import android.content.Context;

import androidx.room.Room;

import com.example.vikingesejllog.AppDatabase;

public class DatabaseBuilder {
    private static AppDatabase db;

    private static AppDatabase get(Context context){
        if(db == null){
            db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "VikingeDB").build();
        }
        return db;
    }
}
