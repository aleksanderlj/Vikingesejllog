package com.example.vikingesejllog.etape;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class CreateEtape extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    List<String> crew;
    AppDatabase db;
    Date departure;
    ImageView crewButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crew = new ArrayList<>();
        db = DatabaseBuilder.get(this);
        setContentView(R.layout.etape_activity_createetape);
        departure = new Date(0L);

        findViewById(R.id.createEtapeDepartureDateBox).setOnClickListener(this);
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

            case R.id.createEtapeDepartureDateBox:
                Calendar c = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(this, this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dp.show();
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
                e.setDeparture(departure);
                e.setCrew(crew);
                e.setTogt_id(getIntent().getLongExtra("togt_id", -1L));

                setResult(Activity.RESULT_OK);

                Executors.newSingleThreadExecutor().execute(() -> {
                    db.etapeDAO().insert(e);
                    finish();
                });
                break;
            case R.id.createEtapeAfbrydBtn:
                setResult(Activity.RESULT_OK);
                finish();


        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        departure = c.getTime();
        String s = "" + dayOfMonth + "-" + (month+1) + "-" + year;
        TextView date = findViewById(R.id.createEtapeDepartureDateText);
        date.setText(s);
    }
}