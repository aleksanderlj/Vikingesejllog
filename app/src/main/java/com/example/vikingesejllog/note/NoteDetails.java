package com.example.vikingesejllog.note;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vikingesejllog.R;

public class NoteDetails extends AppCompatActivity {

    private ImageButton cameraButton, micButton;
    private TextView hastighedBox, vindBox, GPSBox, clockBox,
            antalRoerBox, sejlfoeringBox, sejlStillingBox, kursBox, noteField;

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




    }

}
