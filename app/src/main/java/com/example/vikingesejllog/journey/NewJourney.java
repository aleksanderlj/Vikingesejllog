package com.example.vikingesejllog.journey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vikingesejllog.R;

public class NewJourney extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_harbor);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_journey:
                Intent i = new Intent(this, JourneyList.class);
                startActivity(i);
                break;
        }
    }
}
