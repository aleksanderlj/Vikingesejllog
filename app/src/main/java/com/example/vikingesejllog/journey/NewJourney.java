package com.example.vikingesejllog.journey;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vikingesejllog.R;

public class NewJourney extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_journey);
        findViewById(R.id.confirmButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirmButton:
                this.finish();
                break;
        }
    }
}
