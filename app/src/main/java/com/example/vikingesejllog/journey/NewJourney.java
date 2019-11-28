package com.example.vikingesejllog.journey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.vikingesejllog.R;

public class NewJourney extends AppCompatActivity implements View.OnClickListener {
    TextView start, end;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_journey);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        findViewById(R.id.godkend).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.godkend:
                this.finish();
                break;
        }
    }
}
