package com.example.vikingesejllog.note;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
    private TextView vindBox, GPSBox, clockBox, clockBoxText,
            antalRoerBox, sejlfoeringBox, sejlStillingBox,
            kursBox, noteField;

    private AppDatabase db;

    private Note note;

    //MEDIA SUPPORT:
    private AudioPlayer audioPlayer;
    private ProgressDialog progressDialogAfspiller;
    private File audioFolder, imageFolder, audioFile, imageFile;

    Handler handler = new Handler();
    Runnable audioPlayerTimeUpdate;

    private String fileName, audioDurationString;



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
        clockBoxText = findViewById(R.id.clockBoxText);

        db = DatabaseBuilder.get(this);

        // Hent note ID fra intent, med lille (unødvendig?) fejlsikring
        Intent intent = getIntent();
        long noteId = intent.getLongExtra("noteId", 0);
        if (noteId == 0) {
            Toast.makeText(this, "FEJL: Kunne ikke hente note fra liste", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        // Hent note detaljer fra database og opdater TextViews
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

        // Hent dato fra file name og indsæt i TextView over klokkeslæt
        fileName = intent.getStringExtra("fileName");
        String[] datoSplit = fileName.split("\\.");
        String dato = datoSplit[0] + "/" + datoSplit[1] + "-" + datoSplit[2];
        clockBoxText.setText(dato);

        // Tilføj note nr. og total antal af noter til top fragment
        int noteNumber = intent.getIntExtra("noteNumber", 0);
        int totalNotes = intent.getIntExtra("noteCount", 0);
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
            Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, 275, 275, true);
            cameraButton.setImageBitmap(thumbnail);
            savedPictureZoomed2.setImageBitmap(bitmap);
                //Er nødvendigt da nogen telefoner roterer billedet forkert.. som f.eks. Samsung zzz
                if (bitmap.getHeight() < bitmap.getWidth()){
                    cameraButton.setRotation(90);
                    savedPictureZoomed2.setRotation(90);
                }
        } else {
            cameraButton.setVisibility(View.INVISIBLE);
            cameraButton.setEnabled(false);
        }

        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);

        audioFile = new File(audioFolder + "/" + fileName + ".mp3");
            if(!audioFile.exists()){ //Hvis filen ikke eksisterer så ingen knap:
                playButton = findViewById(R.id.playButton);
                playButton.setOnClickListener(this);
                playButton.setEnabled(false);
                playButton.setVisibility(View.INVISIBLE);}

            else if (audioFile.exists()){
                //Hvis den eksisterer gøres afspilleren klar allerede her,
                // så progressDialogAfspiller kan opdateres med længde på lydfilen:
                audioPlayer = new AudioPlayer();
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object... arg0) {
                        try {
                            audioPlayer.setupAudioPlayer(audioFolder + "/" + fileName + ".mp3");
                            return Log.d("Afspiller", "Følgende lydfil afspilles: " + audioFolder + "/" + fileName + ".mp3");
                        } catch (Exception e) {
                            Toast.makeText(NoteDetails.this, "Indlæsning fejlede - prøv igen", Toast.LENGTH_LONG).show();
                            return Log.d("ERROR", "Det virker IKKE: " + audioFolder + "    " + fileName + e);
                        }
                    }

                    @Override
                    protected void onPostExecute(Object obj) {
                        audioDurationString = audioPlayer.returnDurationString(); //Gemmer længden på filen der skal afspilles, så den kan vises til brugeren senere
                    }
                }.execute();
            }

        Log.d("TEST", (audioFile.exists() + "   " + imageFile + "   " + audioFile + "   " + fileName));
    }

    @Override
    public void onClick(View v) {
        if (v == playButton) {

            audioPlayer.startAudioPlayer();

            progressDialogAfspiller = new ProgressDialog(NoteDetails.this, ProgressDialog.STYLE_SPINNER);
            progressDialogAfspiller.setTitle("Afspiller lydnote...");
            progressDialogAfspiller.setMessage("00:00 / " + audioDurationString);
            progressDialogAfspiller.setCancelable(false);
            progressDialogAfspiller.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop afspilning", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.removeCallbacks(audioPlayerTimeUpdate);
                    audioPlayer.rewindAudioPlayer();
                }
            });
            progressDialogAfspiller.show();
            progressDialogAfspiller.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorWhiteGrey));
            progressDialogAfspiller.getButton(DialogInterface.BUTTON_NEGATIVE).setBackground(getResources().getDrawable(R.drawable.media_player_button_accept));

            new AsyncTask() {
                @Override
                protected Object doInBackground(Object... arg0) {
                    try {
                        while (audioPlayer.isAudioPlaying());
                        return Log.d("Test af lydafspiller: ", audioPlayer.isAudioPlaying() + "");
                    } catch (Exception e){
                        e.printStackTrace();
                        return Log.d("Test af lydafspiller: ", "ProgressDialog kunne ikke lukkes " + audioPlayer.isAudioPlaying() + "  " + e);
                    }}

                @Override
                protected void onPostExecute(Object obj){
                    if (!audioPlayer.isAudioPlaying()){
                        handler.removeCallbacks(audioPlayerTimeUpdate);
                        audioPlayer.rewindAudioPlayer();
                        progressDialogAfspiller.dismiss();}
                }
            }.execute();

            runOnUiThread(audioPlayerTimeUpdate = new Runnable() {
                int currentPlaytime = -1;

                public void run() {
                    if (currentPlaytime++ < 9 && audioPlayer.isAudioPlaying()) {
                        progressDialogAfspiller.setMessage("00:0" + currentPlaytime + "/" + audioDurationString);
                        progressDialogAfspiller.show();
                    } else if (currentPlaytime >= 9 && currentPlaytime <60 && audioPlayer.isAudioPlaying()){
                        progressDialogAfspiller.setMessage("00:" + currentPlaytime + "/" + audioDurationString);
                        progressDialogAfspiller.show();
                    } else if (currentPlaytime >= 59 && currentPlaytime < 300 && audioPlayer.isAudioPlaying()){
                        progressDialogAfspiller.setMessage("0" + currentPlaytime + "/" + audioDurationString); // Format skal lige fikses ved denne
                        progressDialogAfspiller.show();
                    } else if (currentPlaytime >= 300 && audioPlayer.isAudioPlaying()){ //Max 5 minuters optagelse virker rimeligt.
                        handler.removeCallbacks(audioPlayerTimeUpdate);
                        audioPlayer.rewindAudioPlayer();
                    }
                    handler.postDelayed(audioPlayerTimeUpdate, 1000); // et sekund
                }
            });
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == cameraButton && imageFile.exists()){ //"ZOOM"-FUNKTION
            {if (event.getAction() == MotionEvent.ACTION_DOWN) {
                savedPictureZoomed2.setVisibility(View.VISIBLE);
                savedPictureZoomed2.setElevation(100);
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                savedPictureZoomed2.setVisibility(View.INVISIBLE);
                savedPictureZoomed2.setElevation(-1);
                return true;
            }}}
        return false;
    }
}
