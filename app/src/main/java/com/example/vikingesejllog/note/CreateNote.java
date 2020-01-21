package com.example.vikingesejllog.note;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.etape.CrewListItem;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.note.dialogs.NoteDialog;
import com.example.vikingesejllog.note.dialogs.NoteDialogComment;
import com.example.vikingesejllog.note.dialogs.NoteDialogListener;
import com.example.vikingesejllog.note.dialogs.NoteDialogNumberPicker;
import com.example.vikingesejllog.other.DatabaseBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

import im.delight.android.location.SimpleLocation;
import io.sentry.Sentry;

public class CreateNote extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, NoteDialogListener {

    // TODO ryd op i alle findByViewID() declarations (vi behøver ikke finde dem alle fra start)

    //Permissions support:
    private boolean permissionToRecordAccepted;
    private boolean permissionToCamera;
    private boolean permissionToWriteStorage;

    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 100;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 300;


    //ALLE VARIABLE TIL SELVE NOTEN:
    private Button windSpeed, course, sejlforing, sejlStilling, rowers, comment;
    private TextView windSpeedBtnText, courseBtnText, sejlforingBtnText,
            sejlStillingBtnText, rowersBtnText, commentText;
    private ImageButton micButton, cameraButton;

    private MyGPS gps;
    private String gpsData;

    private AppDatabase db;

    private final int WIND_FIELD = 0, ROWERS_FIELD = 1, SAILFORING_FIELD = 2, SAILDIRECTION_FIELD = 3, COURSE_FIELD = 4;


    //AUDIO OG IMAGE VARIABLER:
    private static final String audioTAG = "TEST AF LYDOPTAGER";
    private static final String imageTAG = "TEST AF BILLEDEFUNKTION";

    Handler handler = new Handler();
    Runnable audioRecorderTimeUpdate, audioPlayerTimeUpdate;

    private AudioRecorder audioRecorder;
    private AudioPlayer audioPlayer;

    private File audioFolder, imageFolder, imageFile, audioFile;

    private String fileName, audioDurationString;

    private boolean recordingDone;
    private int numberOfRowers = 0;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView savedPicture, savedPictureZoomed;

    private SimpleLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_createnote);

        db = DatabaseBuilder.get(this);

        findViewById(R.id.createNoteAccepterBtn).setOnClickListener(this);
        findViewById(R.id.createNoteAfbrydBtn).setOnClickListener(this);

        windSpeedBtnText = findViewById(R.id.windSpeedBtnText);
        windSpeed = findViewById(R.id.windspeedBtn);
        windSpeed.setOnClickListener(this);

        course = findViewById(R.id.courseBtn);
        courseBtnText = findViewById(R.id.courseBtnText);
        course.setOnClickListener(this);

        sejlforing = findViewById(R.id.sailforingBtn);
        sejlforingBtnText = findViewById(R.id.sejlfoeringBtnText);
        sejlforing.setOnClickListener(this);

        sejlStilling = findViewById(R.id.sailStillingBtn);
        sejlStillingBtnText = findViewById(R.id.sejlstillingBtnText);
        sejlStilling.setOnClickListener(this);

        rowers = findViewById(R.id.rowerCountBtn);
        rowersBtnText = findViewById(R.id.rowersBtnText);
        rowers.setOnClickListener(this);

        comment = findViewById(R.id.commentButton);
        commentText = findViewById(R.id.commentText);
        comment.setOnClickListener(this);

        gps = new MyGPS(this);
        location = gps.getLocation();
        location.beginUpdates();


        //Gør lydoptageren og lydafspilleren klar:
        audioRecorder = new AudioRecorder();
        audioPlayer = new AudioPlayer();


        // Vigtigt at der her er noget, der aflæser om noten har et billede
        // gemt sammen med dens database objekt, således at det bliver muligt, at bestemme
        // om cameraButton skal være med "Kamera"-ikon eller bitmap af det gemte billede.
        cameraButton = findViewById(R.id.createNoteCameraBtn);
        cameraButton.setOnClickListener(this);

        savedPicture = findViewById(R.id.savedPicture);
        //Et eller andet med if(!imageTaken) så er det usynligt:
//        savedPicture.setVisibility(View.INVISIBLE);

        //savedPictureZoomed er et større ImageView gemt bagerst, der bliver visible,
        // når brugeren holder fingeren nede på savedPicture som en slags zoom-funktion
        savedPictureZoomed = findViewById(R.id.savedPictureZoomed);
        savedPictureZoomed.setVisibility(View.INVISIBLE);

        // Vigtigt at der her er noget, der aflæser om noten har en lydoptagelse
        // gemt sammen med dens database objekt vha. recordingDone, således at det bliver muligt,
        // at bestemme om micButton skal være med "Play" eller "Mikrofon"-ikon.
        micButton = findViewById(R.id.createNoteMicBtn);
        micButton.setOnClickListener(this);


        //Her skal der tjekkes for en instans af fileName inde i databasen, således at hvis der
        //allerede findes en fileName, skal denne bruges i stedet for at oprette et nyt, således
        //at det bliver muligt, at finde tidligere gemte billeder og lydfiler i telefonens hukommelse.
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss", Locale.getDefault());
        fileName = sdf.format(new Date());

        Log.d("Aktuelle filnavn: ", fileName);


        //Gør mappen for lydnoter samt lydfil klar:
        audioFolder = new File(Environment.getExternalStorageDirectory() + "/Sejllog/Lydnoter/");
        audioFile = new File(audioFolder + "/" + fileName + ".mp3");
        //Gør mappen for billeder klar:
        imageFolder = new File(Environment.getExternalStorageDirectory() + "/Sejllog/Billedenoter/");

        ActivityCompat.requestPermissions(this, permissions, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        location.beginUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        location.endUpdates();
    }

    public void setWindSpeed() {
        String[] s1 = {"N", "NNØ", "NØ", "ØNØ", "Ø", "ØSØ", "SØ", "SSØ", "S", "SSV", "SV", "VSV", "V", "VNV", "NV", "NNV"};

        int range = 33;
        String[] s2 = new String[range + 1];
        for (int n = 0; n < range; n++) {
            s2[n] = n + " m/s";
        }
        s2[s2.length - 1] = "33+";


        // TODO Direction skal gerne vælges fra et kompas, så skal nok lave en anden klasse
        NoteDialog df = new NoteDialogNumberPicker(WIND_FIELD, s1, s2);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "wind");
    }

    public void setCourse() {
        int range = 10;
        String[] s1 = {"0", "1", "2", "3"},
                s2 = new String[range],
                s3 = new String[range];

        for (int n = 0; n < range; n++) {
            s2[n] = String.valueOf(n);
            s3[n] = String.valueOf(n);
        }

        NoteDialog df = new NoteDialogNumberPicker(COURSE_FIELD, s1, s2, s3);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "wind");
    }

    public void setSailForing() {
        String[] s = {"F", "Ø", "N1", "N2", "N3"};

        NoteDialog df = new NoteDialogNumberPicker(SAILFORING_FIELD, s);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "sailforing");
    }

    public void setSailStilling() {
        String[] s1 = {"bi", "fo", "ha", "ag", "læ"};
        String[] s2 = {"sb", "bb"};

        NoteDialog df = new NoteDialogNumberPicker(SAILDIRECTION_FIELD, s1, s2);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "saildirection");
    }

    public void setRowers() {

        numberOfRowers = getIntent().getIntExtra("crew_size",20);

        // defaults to 20 if no crewList is given
        if (numberOfRowers == 0)
            numberOfRowers = 20;

        String[] s = new String[numberOfRowers + 1];
        for (int n = 0; n < numberOfRowers + 1; n++) {
            s[n] = String.valueOf(n);
        }

        NoteDialog df = new NoteDialogNumberPicker(ROWERS_FIELD, s);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "rowers");
    }

    public void setComment(){
        NoteDialog cd = new NoteDialogComment(commentText.getText().toString());
        cd.setNoteDialogListener(this);
        cd.show(getSupportFragmentManager().beginTransaction(), "comment");
    }

    public void confirm() {
        location = gps.getLocation();

        gpsData = "LAT: " + String.format(Locale.US, "%.2f", location.getLatitude()) + "\n" +
                "LON: " + String.format(Locale.US, "%.2f", location.getLongitude());

        SimpleDateFormat clock = new SimpleDateFormat("HH.mm", Locale.getDefault());
        String time = clock.format(new Date());

        boolean hasComment = false, hasImage = false, hasAudio = false;

        if(!commentText.getText().toString().equals("")){
            hasComment = true;
        }

        if(audioFile != null && audioFile.exists()){
            hasAudio = true;
        }

        if(imageFile != null && imageFile.exists()){
            hasImage = true;
        }


        Note note = new Note(getIntent().getLongExtra("etape_id", -1L), gpsData,
                windSpeedBtnText.getText().toString(),
                time,
                rowersBtnText.getText().toString(),
                sejlforingBtnText.getText().toString(),
                sejlStillingBtnText.getText().toString(),
                courseBtnText.getText().toString(),
                commentText.getText().toString(),
                hasComment,
                hasImage,
                hasAudio,
                fileName);

        Executors.newSingleThreadExecutor().execute(() -> {
            db.noteDAO().insert(note);
            setResult(Activity.RESULT_OK);
            finish();
        });
    }


    //Implementering af AudioRecorder, AudioPlayer og kamerafunktionalitet:
    @Override
    public void onClick(View v) {
        ProgressDialog progressDialogOptager;
        ProgressDialog progressDialogAfspiller;

        switch (v.getId()){
            case R.id.windspeedBtn:
                setWindSpeed();
                break;

            case R.id.sailforingBtn:
                setSailForing();
                break;

            case R.id.rowerCountBtn:
                setRowers();
                break;

            case R.id.sailStillingBtn:
                setSailStilling();
                break;

            case R.id.courseBtn:
                setCourse();
                break;

            case R.id.commentButton:
                setComment();
                break;

            case R.id.createNoteMicBtn:
                ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

                if (!recordingDone) {
                    new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object... arg0) {
                            try {
                                audioRecorder.setupAudioRecord(audioFile.toString());
                                return Log.d(audioTAG, "Der gemmes en lydfil i: " + audioFile);
                            } catch (Exception e) {
                                Sentry.capture(e);
                                e.printStackTrace();
                                return Log.d(audioTAG, "Det virker IKKE: " + e);
                            }
                        }

                        @Override
                        protected void onPostExecute(Object obj) {
                            audioRecorder.startAudioRecord();
                        }
                    }.execute();

                    progressDialogOptager = new ProgressDialog(CreateNote.this, ProgressDialog.STYLE_SPINNER);
                    progressDialogOptager.setTitle("Optager lydnote...");
                    progressDialogOptager.setMessage("00:00");
                    progressDialogOptager.setCancelable(false);
                    progressDialogOptager.setButton(DialogInterface.BUTTON_NEGATIVE, "Gem optagelse", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            handler.removeCallbacks(audioRecorderTimeUpdate);
                            audioRecorder.stopAudioRecord();
                            recordingDone = true;
                            //Skifter ikon til "PLAY"-knap.
                            ((ImageView) findViewById(R.id.createNoteMicBtn)).setImageResource(R.drawable.play);

                            //Er nødvendigt at gøre AudioPlayer klar her, da progressdialog ellers
                            // ikke opdateres med duration på filen, når den afspilles første gang!
                            new AsyncTask() {
                                @Override
                                protected Object doInBackground(Object... arg0) {
                                    try {
                                        audioPlayer.setupAudioPlayer(audioFile.toString());
                                        return Log.d(audioTAG, "Følgende lydfil klargøres: " + audioFile);
                                    } catch (Exception e){
                                        Sentry.capture(e);
                                        e.printStackTrace();
                                        return Log.d(audioTAG, "Fejl i afspiller: " + audioFolder + "    " + fileName + e);
                                    }}

                                @Override
                                protected void onPostExecute(Object obj){
                                    audioDurationString = audioPlayer.returnDurationString(); //Gemmer længden på filen der skal afspilles, så den kan vises senere
                                }
                            }.execute();

                            Toast.makeText(CreateNote.this, "Lydnoten blev gemt i mappen: " + audioFolder, Toast.LENGTH_SHORT).show();
                        }
                    });
                    progressDialogOptager.show();
                    progressDialogOptager.getButton(DialogInterface.BUTTON_NEGATIVE).setBackground(getResources().getDrawable(R.drawable.media_player_button_accept));
                    progressDialogOptager.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorWhiteGrey));

                    //Sørger for at timeren tæller op, så brugeren kan se hvor lang optagelsen er:
                    runOnUiThread(audioRecorderTimeUpdate = new Runnable() {
                        int currentRecordTime = 0;

                        public void run() {
                            String currentRecordTimeString = String.format("%02d:%02d",
                                    currentRecordTime/60, currentRecordTime % 60);

                            if (currentRecordTime++ <= 100000) {
                                progressDialogOptager.setMessage(currentRecordTimeString);
                                progressDialogOptager.show();
                            }
                            handler.postDelayed(audioRecorderTimeUpdate, 1000); // hvert sekund
                        }
                    });
                }

                if (recordingDone) {
                    //Starter afspilleren:
                    audioPlayer.startAudioPlayer();


                    progressDialogAfspiller = new ProgressDialog(CreateNote.this, ProgressDialog.STYLE_SPINNER);
                    progressDialogAfspiller.setTitle("Afspiller lydnote..");
                    progressDialogAfspiller.setMessage("00:00 / " + audioDurationString);
                    progressDialogAfspiller.setCancelable(false);
                    progressDialogAfspiller.setButton(DialogInterface.BUTTON_NEUTRAL, "SLET", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) { //Så lydnoten kan tages om:
                            audioPlayer.resetAudioPlayer();
                            recordingDone = false;
                            ((ImageView) findViewById(R.id.createNoteMicBtn)).setImageResource(R.drawable.mic);
                            File deleteAudioFile = new File(audioFile.toString());
                            deleteAudioFile.delete(); //Sletter filen i hukommelsen
                        }
                    });
                    progressDialogAfspiller.setButton(DialogInterface.BUTTON_POSITIVE, "Stop afspilning", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.removeCallbacks(audioPlayerTimeUpdate);
                            audioPlayer.rewindAudioPlayer(); //Går tilbage til 00:00
                        }
                    });
                    progressDialogAfspiller.show(); //Er nødt til at hardcode farverne, da theme ikke fungerer ordentligt:
                    progressDialogAfspiller.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorWhiteGrey));
                    progressDialogAfspiller.getButton(DialogInterface.BUTTON_POSITIVE).setBackground(getResources().getDrawable(R.drawable.media_player_button_accept));
                    progressDialogAfspiller.getButton(DialogInterface.BUTTON_NEUTRAL).setBackground(getResources().getDrawable(R.drawable.media_player_button_negative));
                    progressDialogAfspiller.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorWhiteGrey));
                    //Asynctask der holder øje med om afspilleren stadigvæk spiller lyd, og hvis den
                    // ikke gør det, så lukkes progressDialogAfspiller ned i stedet for brugeren
                    // selv skal trykke afslut:
                    new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object... arg0) {
                            try {
                                while (audioPlayer.isAudioPlaying());
                                return Log.d(audioTAG, audioPlayer.isAudioPlaying() + "");
                            } catch (Exception e){
                                Sentry.capture(e);
                                e.printStackTrace();
                                return Log.d(audioTAG, "ProgressDialog kunne ikke lukkes " + audioPlayer.isAudioPlaying() + "  " + e);
                            }}

                        @Override
                        protected void onPostExecute(Object obj){
                            if (!audioPlayer.isAudioPlaying()){
                                handler.removeCallbacks(audioPlayerTimeUpdate);
                                audioPlayer.rewindAudioPlayer();
                                progressDialogAfspiller.dismiss();}
                        }
                    }.execute();

                    //Sørger for at timeren opdaterer så brugeren kan se, hvor langt lydnoten er, samt hvor meget af den, der er afspillet:
                    runOnUiThread(audioPlayerTimeUpdate = new Runnable() {
                        int currentPlayTime = 0;

                        public void run() {
                            String currentPlayTimeString = String.format("%02d:%02d",
                                    currentPlayTime/60, currentPlayTime % 60);

                            if (currentPlayTime++ <= 100000  && audioPlayer.isAudioPlaying()) {
                                progressDialogAfspiller.setMessage(currentPlayTimeString + "/" + audioDurationString);
                                progressDialogAfspiller.show();
                            }
                            handler.postDelayed(audioPlayerTimeUpdate, 1000); // hvert sekund
                        }
                    });
                }
                break;


            case R.id.createNoteCameraBtn:
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA_PERMISSION);

                //Ny fil der kan laves til en Uri efterfølgende:
                imageFile = new File(imageFolder + "/" + fileName + ".jpg");
                Uri imageURI = FileProvider.getUriForFile(this, "com.example.vikingesejllog.fileprovider", imageFile);

                //Udskriver stien til mig i terminalen:
                Log.d(imageTAG, imageFile.getAbsolutePath());

                //Sender intent til at åbne kameraet og afventer resultatet.
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Tjekker om mobilen har et kamera:
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    //imageTaken = true; //Så kun et billede kan gemmes pr. note!
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);}
                break;

            case R.id.createNoteAccepterBtn:
                if(audioPlayer != null){
                    audioPlayer.endAudioPlayer();}
                confirm();
                break;

            case R.id.createNoteAfbrydBtn:
                if(audioPlayer != null){
                    audioPlayer.endAudioPlayer();}
                if (imageFile != null && imageFile.exists()){
                    imageFile.delete(); }
                if (audioFile != null && audioFile.exists()){
                    audioFile.delete();
                }
                finish();
                break;
        }}

    //Checker om permissions er gemt.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if (!permissionToRecordAccepted){
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;}
            case REQUEST_CAMERA_PERMISSION:
                if (!permissionToCamera){
                permissionToCamera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;}
            case REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (!permissionToWriteStorage){
                permissionToWriteStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;}

                //Opretter mapperne på telefonen, når brugeren godkender denne permission!
                if (!audioFolder.exists()) {
                        try {
                            audioFolder.mkdirs();
                            audioFolder.createNewFile();
                        } catch (Exception e) {
                            Sentry.capture(e);
                            e.printStackTrace();
                        }
                    }
                if (!imageFolder.exists()) {
                    try {
                        imageFolder.mkdirs();
                        imageFolder.createNewFile();
                    } catch (Exception e) {
                        Sentry.capture(e);
                        e.printStackTrace();
                    }
                }
                break;
        }}

    //Først EFTER kameraet har kørt, oprettes der et BitmapFactory, der omdanner den gemte billedefil
    // til et bitmap, der kan vise en miniature udgave af billedet inde i noten:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Køres når der er et resultat fra kamera appen og gemmer det som et bitmap:
        super.onActivityResult(REQUEST_IMAGE_CAPTURE, resultCode, data);

        if (resultCode == RESULT_OK) { //Så billedet kun vises, hvis brugeren trykke OK og ikke bare back-button
            Toast.makeText(CreateNote.this, "Det originale billede blev gemt i mappen: " + imageFolder, Toast.LENGTH_SHORT).show();

            //Gemmer billedet som et bitmap ud fra imageFile filen, således billedet også kan vises i appen.
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.toString());
            if (bitmap != null) {
                savedPicture.setImageBitmap(bitmap);
                    //Er nødvendigt da nogen telefoner roterer billedet forkert.. som f.eks. Samsung....
                    if (bitmap.getHeight() < bitmap.getWidth()) {
                        savedPicture.setRotation(90);
                        savedPictureZoomed.setRotation(90);
                    }
                savedPicture.setVisibility(View.VISIBLE);
                savedPicture.setOnTouchListener(this);
                savedPictureZoomed.setImageBitmap(bitmap);
            }
        }
    }

    //Zoom ind på billede bitmap ved at røre det:
    @Override
    public boolean onTouch(View savedPicture, MotionEvent event) {
    /*Zoomer ind på billedet, hvis brugeren rør ved det, og zoomer ud igen,
    hvis fingeren slippes
     */
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                savedPictureZoomed.setElevation(1000);
                savedPictureZoomed.setVisibility(View.VISIBLE);
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                savedPicture.setElevation(-1);
                savedPictureZoomed.setVisibility(View.INVISIBLE);
                return true;
            }
        return false;
    }

    //Til valg af værdi på notefelterne:
    @Override
    public void onNumberPickerSelected(String[] values, int field) {
        String s;

        switch (field) {
            case ROWERS_FIELD:
                rowersBtnText.setText(values[0]);
                break;

            case SAILFORING_FIELD:
                sejlforingBtnText.setText(values[0]);
                break;

            case WIND_FIELD:
                s = values[0] + " " + values[1];
                windSpeedBtnText.setText(s);
                break;

            case SAILDIRECTION_FIELD:
                s = values[0] + " " + values[1];
                sejlStillingBtnText.setText(s);
                break;

            case COURSE_FIELD:
                s = values[0] + values[1] + values[2] + "\u00B0";
                courseBtnText.setText(s);
                break;
        }

    }

    @Override
    public void onNumberPickerDelete(int field) {
        switch (field) {
            case ROWERS_FIELD:
                rowersBtnText.setText("");
                break;

            case SAILFORING_FIELD:
                sejlforingBtnText.setText("");
                break;

            case WIND_FIELD:
                windSpeedBtnText.setText("");
                break;

            case SAILDIRECTION_FIELD:
                sejlStillingBtnText.setText("");
                break;

            case COURSE_FIELD:
                courseBtnText.setText("");
                break;
        }
    }

    @Override
    public void onCommentSelected(String comment) {
        commentText.setText(comment);
    }
}
