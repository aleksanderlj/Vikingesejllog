package com.example.vikingesejllog.journey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.R;

public class JourneyList extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_list);
        findViewById(R.id.newHarborButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newHarborButton:
                Intent i = new Intent(this, NewJourney.class);
                startActivity(i);
                break;
        }
    }
}
