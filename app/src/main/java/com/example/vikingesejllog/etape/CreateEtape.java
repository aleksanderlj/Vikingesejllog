package com.example.vikingesejllog.etape;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.other.DatabaseBuilder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;

public class CreateEtape extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> crew;
    AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseBuilder.get(this);
        setContentView(R.layout.etape_activity_createetape);

        /*TODO tests
        crew = new ArrayList<>();
        crew.add("Max");
        crew.add("Alek");
        crew.add("Freddy Fazbear");
         */

        findViewById(R.id.createEtapeCrewCountBox).setOnClickListener(this);
        findViewById(R.id.createEtapeAccepterBtn).setOnClickListener(this);
        findViewById(R.id.createEtapeAfbrydBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.createEtapeCrewCountBox:
                Intent i = new Intent(this, CrewList.class);
                startActivity(i);
                break;

            case R.id.createEtapeAccepterBtn:
                Etape e = new Etape();
                EditText skipper = findViewById(R.id.skipperNameEditText);
                EditText start = findViewById(R.id.createEtapeDepartureText);
                EditText end = findViewById(R.id.createEtapeArrivalText);
//                EditText date = findViewById(R.id.createEtapeDepartureDateBox);

                e.setSkipper(skipper.getText().toString());
                e.setStart(start.getText().toString());
                e.setEnd(end.getText().toString());
                //TODO Lav dato halløj
                e.setDeparture(new Date());
                e.setTogt_id(getIntent().getLongExtra("togt_id", -1L));

                setResult(Activity.RESULT_OK);

                Executors.newSingleThreadExecutor().execute(() -> {
                    db.etapeDAO().insert(e);
                    finish();
                });
                break;
            case R.id.createEtapeAfbrydBtn:
                finish();

        }
    }
}
