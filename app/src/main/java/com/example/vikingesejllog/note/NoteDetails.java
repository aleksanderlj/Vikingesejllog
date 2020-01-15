package com.example.vikingesejllog.note;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.io.File;
import java.util.concurrent.Executors;

public class NoteDetails extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private ImageButton cameraButton, playButton;
    private ImageView savedPictureZoomed2;
    private TextView vindBox, GPSBox, clockBox,
            antalRoerBox, sejlfoeringBox, sejlStillingBox, kursBox, noteField;
    private int noteNumber, totalNotes;

    private AppDatabase db;

    private Note note;

    //MEDIA SUPPORT:
    private AudioPlayer audioPlayer;
    private ProgressDialog progressDialog;
    private File audioFolder, imageFolder, audioFile, imageFile;

    private String fileName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        vindBox = findViewById(R.id.vindBox);
        GPSBox = findViewById(R.id.GPSBox);
        clockBox = findViewById(R.id.clockBox);
        antalRoerBox = findViewById(R.id.antalRoereBox);
        sejlfoeringBox = findViewById(R.id.sejlføringBox);
        sejlStillingBox = findViewById(R.id.sejlstillingBox);
        kursBox = findViewById(R.id.kursBox);
        noteField = findViewById(R.id.noteField);

        db = DatabaseBuilder.get(this);

        Intent intent = getIntent();
        long noteId = intent.getLongExtra("noteId", 0);
        if (noteId == 0) {
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
            });
        });


        noteNumber = intent.getIntExtra("noteNumber", 0);
        totalNotes = intent.getIntExtra("noteCount", 0);
        fileName = intent.getStringExtra("fileName");

        NoteDetailsTopFragment topFragment = (NoteDetailsTopFragment) getSupportFragmentManager().findFragmentById(R.id.noteDetailsTopFragment);
        topFragment.updateTextView("Note " + noteNumber + "/" + totalNotes);


        //MEDIA SUPPORT:
        imageFolder = new File(Environment.getExternalStorageDirectory() + "/Sejllog/Billedenoter/");
        audioFolder = new File(Environment.getExternalStorageDirectory() + "/Sejllog/Lydnoter/");

        cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnTouchListener(this);

        //Sætter det gemte billede som ikon, hvis det findes:
        imageFile = new File(imageFolder + "/" + fileName + ".jpg");
        if (imageFile.exists()) {
            //Gem billede for zoom funktionalitet i baggrunden, hvis det findes:
            savedPictureZoomed2 = findViewById(R.id.savedPictureZoomed2);
            savedPictureZoomed2.setVisibility(View.INVISIBLE);

            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.toString());
            cameraButton.setImageBitmap(bitmap);
            savedPictureZoomed2.setImageBitmap(bitmap);
            cameraButton.setRotation(90);
            savedPictureZoomed2.setRotation(90);
        }

        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        audioFile = new File(audioFolder + "/" + fileName + ".mp3");

        Log.d("TEST", (audioFile.exists() + "   " + imageFile + "   " + audioFile + "   " + fileName));
    }

    @Override
    public void onClick(View v) {
        if (v == playButton) {
            if (audioFile.exists()) {
                audioPlayer = new AudioPlayer();
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object... arg0) {
                        try {
                            audioPlayer.setupAudioPlayer(audioFolder + "/" + fileName + ".mp3");
                            return Log.d("Afspiller", "Følgende lydfil afspilles: " + audioFolder + "/" + fileName + ".mp3");
                        } catch (Exception e) {
                            Toast.makeText(NoteDetails.this, "Indlæsning fejlede - prøv igen", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            return Log.d("ERROR", "Det virker IKKE: " + audioFolder + "    " + fileName + e);
                        }
                    }

                    @Override
                    protected void onPostExecute(Object obj) {
                        audioPlayer.startAudioPlayer();
                    }
                }.execute();

                progressDialog = new ProgressDialog(NoteDetails.this);
                progressDialog.setMax(200);
                progressDialog.setTitle("Afspiller lydnote...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Afslut afspilning", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        audioPlayer.stopAudioNote();
                    }
                });
                progressDialog.show();
            }

            //Hvis der ikke er en gemt lydfil tilknyttet noten:
            else if (!audioFile.exists()) {
                Toast.makeText(NoteDetails.this, "Der er ingen gemt lydnote!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == cameraButton && imageFile.exists()){ //"ZOOM"-FUNKTION
            {if (event.getAction() == MotionEvent.ACTION_DOWN) {
                cameraButton.setVisibility(View.INVISIBLE);
                savedPictureZoomed2.setVisibility(View.VISIBLE);
                savedPictureZoomed2.setElevation(100);
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                cameraButton.setVisibility(View.VISIBLE);
                savedPictureZoomed2.setVisibility(View.INVISIBLE);
                savedPictureZoomed2.setElevation(-1);
                return true;
            }}}
        else if (v == cameraButton && !imageFile.exists()) { //Hvis billedet ikke findes:
                Toast.makeText(NoteDetails.this, "Der er intet gemt billede!", Toast.LENGTH_SHORT).show();
                return true;
            }
        return false;
    }
}
