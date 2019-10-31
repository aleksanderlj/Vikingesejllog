package com.example.vikingesejllog.journey;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.R;

public class JourneyList extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_list);
        findViewById(R.id.new_journey).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_journey:
                Intent i = new Intent(this, NewJourney.class);
                startActivity(i);
                break;
        }
    }
}
