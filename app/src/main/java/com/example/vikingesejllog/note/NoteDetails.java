package com.example.vikingesejllog.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vikingesejllog.R;

import java.text.DecimalFormat;

public class NoteDetails extends AppCompatActivity {

    private ImageButton cameraButton, micButton;
    private TextView hastighedBox, vindBox, GPSBox, clockBox,
            antalRoerBox, sejlfoeringBox, sejlStillingBox, kursBox, noteField;
    private int noteNumber, totalNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        cameraButton = findViewById(R.id.cameraButton);
        micButton = findViewById(R.id.micButton);
        hastighedBox = findViewById(R.id.hastighedBox);
        vindBox = findViewById(R.id.vindBox);
        GPSBox = findViewById(R.id.GPSBox);
        clockBox = findViewById(R.id.klokkeslætBox);
        antalRoerBox = findViewById(R.id.antalRoereBox);
        sejlfoeringBox = findViewById(R.id.sejlføringBox);
        sejlStillingBox = findViewById(R.id.sejlstillingBox);
        kursBox = findViewById(R.id.kursBox);
        noteField = findViewById(R.id.noteField);

        Intent intent = getIntent();
        hastighedBox.setText(intent.getStringExtra("boatSpeed"));
        vindBox.setText(intent.getStringExtra("windSpeed"));

        // Formatering af GPS koordinater til maks 3 cifre decimaltal
        String gpsText = intent.getStringExtra("gpsLoc");
        // If-statement checker om "-" er med i gps koordinater. Hvis den ikke er med er det test koordinater.
        if (gpsText != null && gpsText.contains("-")){
            DecimalFormat df = new DecimalFormat("0.00#");
            String[] gpsCoords = gpsText.split("-");
            double latitudeDouble = Double.valueOf(gpsCoords[0]);
            double longitudeDouble = Double.valueOf(gpsCoords[1]);
            String gpsLatitude = df.format(latitudeDouble);
            String gpsLongitude = df.format(longitudeDouble);
            String formattedCoords = gpsLatitude + "-" + gpsLongitude;
            GPSBox.setText(formattedCoords);
        } else {
            GPSBox.setText(gpsText);
        }

        clockBox.setText(intent.getStringExtra("time"));
        antalRoerBox.setText(intent.getStringExtra("rowers"));
        sejlfoeringBox.setText(intent.getStringExtra("sailForing"));
        sejlStillingBox.setText(intent.getStringExtra("sailStilling"));
        kursBox.setText(intent.getStringExtra("course"));
        noteField.setText(intent.getStringExtra("comment"));
        noteNumber = intent.getIntExtra("noteNumber", 0);
        totalNotes = intent.getIntExtra("noteCount", 0);

        NoteDetailsTopFragment topFragment = (NoteDetailsTopFragment) getSupportFragmentManager().findFragmentById(R.id.noteDetailsTopFragment);
        topFragment.updateTextView("Note " + noteNumber + "/" + totalNotes);

    }

}
