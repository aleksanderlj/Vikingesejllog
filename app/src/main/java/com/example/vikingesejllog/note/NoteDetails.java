package com.example.vikingesejllog.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.util.concurrent.Executors;

public class NoteDetails extends AppCompatActivity implements View.OnClickListener {

    private ImageButton cameraButton, micButton;
    private TextView hastighedBox, vindBox, GPSBox, clockBox,
            antalRoerBox, sejlfoeringBox, sejlStillingBox, kursBox, noteField;
    private int noteNumber, totalNotes;

    private AppDatabase db;

    private String fileName;
    private Note note;

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

        db = DatabaseBuilder.get(this);

        Intent intent = getIntent();
        long noteId = intent.getLongExtra("noteId", 0);
        if (noteId == 0){
            Toast.makeText(this, "FEJL: Kunne ikke hente note fra liste", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }


        Executors.newSingleThreadExecutor().execute(() -> {
            note = db.noteDAO().getById(noteId);
            runOnUiThread(() -> {
                vindBox.setText(note.getWindSpeed());
                GPSBox.setText(note.getGpsLoc());
                clockBox.setText(note.getTime());
                antalRoerBox.setText(note.getRowers());
                sejlfoeringBox.setText(note.getSailForing());
                sejlStillingBox.setText(note.getSailStilling());
                kursBox.setText(note.getCourse());
                noteField.setText(note.getComment());
                fileName = note.getFileName();
            });
        });

        noteNumber = intent.getIntExtra("noteNumber", 0);
        totalNotes = intent.getIntExtra("noteCount", 0);

        micButton.setOnClickListener(this);


        NoteDetailsTopFragment topFragment = (NoteDetailsTopFragment) getSupportFragmentManager().findFragmentById(R.id.noteDetailsTopFragment);
        topFragment.updateTextView("Note " + noteNumber + "/" + totalNotes);

    }

    @Override
    public void onClick(View v) {
        if (v == micButton){
                Toast.makeText(NoteDetails.this, "Test af fileName: " + fileName, Toast.LENGTH_SHORT).show();
        }
        if (v == cameraButton){
            Toast.makeText(NoteDetails.this, "Test af fileName: " + fileName, Toast.LENGTH_SHORT).show();
        }
    }
}
