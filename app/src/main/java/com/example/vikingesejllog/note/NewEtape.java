package com.example.vikingesejllog.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.R;

import java.util.ArrayList;

public class NewEtape extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> crew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_harbor);
        crew = new ArrayList<>();
        crew.add("Max");
        crew.add("Alek");
        crew.add("Freddy Fazbear");

        findViewById(R.id.crewCountButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.crewCountButton:
                // TODO: Intent i = new Intent();
                break;
        }
    }
}
