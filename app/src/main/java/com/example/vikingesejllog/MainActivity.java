package com.example.vikingesejllog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.vikingesejllog.journey.JourneyList;
import com.example.vikingesejllog.journey.NewJourney;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this, JourneyList.class);
        startActivity(i);
    }
}
