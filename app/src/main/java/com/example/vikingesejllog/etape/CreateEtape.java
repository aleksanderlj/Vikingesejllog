package com.example.vikingesejllog.etape;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.note.NoteList;
import com.example.vikingesejllog.other.DatabaseBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class CreateEtape extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    ArrayList<CrewListItem> crew;
    ArrayList<String> crewNames;
    AppDatabase db;
    Date departure;
    TextView crewCountText;
    EditText skipperText, departureText;
    long etapeId;
    EtapeWithNotes lastEtapeWithNotes;
    Etape lastEtape;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crew = new ArrayList<>();
        db = DatabaseBuilder.get(this);
        setContentView(R.layout.etape_activity_createetape);
        departure = new Date(0L);
        crewCountText = findViewById(R.id.createEtapeCrewCountText);
        skipperText = findViewById(R.id.skipperNameEditText);
        departureText = findViewById(R.id.createEtapeDepartureText);

        findViewById(R.id.createEtapeDepartureDateBox).setOnClickListener(this);
        findViewById(R.id.createEtapeCrewCountBox).setOnClickListener(this);
        findViewById(R.id.createEtapeAccepterBtn).setOnClickListener(this);
        findViewById(R.id.createEtapeAfbrydBtn).setOnClickListener(this);

        Intent intent = getIntent();
        etapeId = intent.getLongExtra("etape_id", -1);

        // Henter kun data fra sidste etape, hvis der blev sendt nogen over igennem intent.
        // Etape id kan aldrig være -1, dvs if-statement her skippes kun ved default value i getLongExtra
        if (etapeId != -1){
            getDataFromLastEtape();
        }

        updateCrewCountText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createEtapeCrewCountBox:
                Intent i = new Intent(this, CrewList.class);

                // Overfør en liste af CrewListItems, så besætning kan redigeres efter den er oprettet.
                Gson gson = new Gson();
                String json = gson.toJson(crew);
                i.putExtra("crew", json);
                startActivityForResult(i, 1);

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

                crewNames = crewItemsToString(crew);

                e.setSkipper(skipper.getText().toString());
                e.setStart(start.getText().toString());
                e.setEnd(end.getText().toString());
                e.setDeparture(departure);
                e.setCrew(crewNames);
                e.setTogt_id(getIntent().getLongExtra("togt_id", -1L));

                setResult(Activity.RESULT_OK);

                if (getIntent().getIntExtra("from_togt",0) == 1) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        db.etapeDAO().insert(e);
                        Intent noteList = new Intent(this, NoteList.class);
                        noteList.putExtra("togt_id",getIntent().getLongExtra("togt_id",-1L));
                        startActivityForResult(noteList, 1);
                        finish();
                    });
                }else {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        db.etapeDAO().insert(e);
                        finish();
                    });
                }
                break;
            case R.id.createEtapeAfbrydBtn:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        departure = c.getTime();
        String s = "" + dayOfMonth + "-" + (month + 1) + "-" + year;
        TextView date = findViewById(R.id.createEtapeDepartureDateText);
        date.setText(s);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Hent crewListen fra CrewList klassen når besætning tilføjes og bruger vender tilbage til CreateEtape.
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String json = data.getStringExtra("crewList");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CrewListItem>>() {
            }.getType();
            crew = gson.fromJson(json, type);
        }
        updateCrewCountText();
    }

    // Converterer en liste af CrewlistItem til en liste af Strings.
    private ArrayList<String> crewItemsToString(ArrayList<CrewListItem> crewListItems) {
        ArrayList<String> crewNames = new ArrayList<>();
        for (int i = 0; i < crewListItems.size(); i++) {
            crewNames.add(crewListItems.get(i).getCrewMember());
        }
        return crewNames;
    }

    // Besætningsantal opdateres når en besætning haves
    private void updateCrewCountText(){
        if (crew.isEmpty()){
            crewCountText.setText("Tilføj øvrig besætning");
        } else {
            crewCountText.setText("Antal øvrig besætning: " + crew.size());
        }
    }

    // Henter sidste etape i togt fra NoteList klassen, når en ny etape oprettes.
    // Herfra kan besætningslisten fra sidste etape autofyldes i den nye etape besætningsliste.
    private void getDataFromLastEtape(){
        Executors.newSingleThreadExecutor().execute(() -> {
            lastEtapeWithNotes = db.etapeDAO().getById(etapeId);
            lastEtape = lastEtapeWithNotes.getEtape();
            List<String> lastEtapeCrewNames;
            lastEtapeCrewNames = lastEtape.getCrew();
            ArrayList<CrewListItem> crewConvertedToItems = new ArrayList<>();
            for (int i = 0; i < lastEtapeCrewNames.size(); i++){
                CrewListItem crewListItem = new CrewListItem(lastEtapeCrewNames.get(i));
                crewConvertedToItems.add(crewListItem);
            }
            crew = crewConvertedToItems;

            runOnUiThread(() -> {
                updateCrewCountText();

                // Autofyld skipper navn med skipper fra sidste etape
                skipperText.setText(lastEtape.getSkipper());

                // Autofyld afgang med destination fra sidste etape
                departureText.setText(lastEtape.getEnd());
            });
        });
    }
}