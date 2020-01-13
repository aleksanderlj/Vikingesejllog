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
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.note.dialogs.NoteDialog;
import com.example.vikingesejllog.note.dialogs.NoteDialogListener;
import com.example.vikingesejllog.note.dialogs.NoteDialogNumberPicker;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CreateNote extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, NoteDialogListener {

    // TODO ryd op i alle findByViewID() declarations (vi behøver ikke finde dem alle fra start)

    //Permissions support:
    private boolean permissionToRecordAccepted;
    private boolean permissionToCamera;
    private boolean permissionToReadStorage;
    private boolean permissionToWriteStorage;

    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_CAMERA_PERMISSION = 300;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 400;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 500;


    //ALLE VARIABLE TIL SELVE NOTEN:
    private Button windSpeed, course, sejlforing, sejlStilling, rowers, micButton, cameraButton;
    private EditText commentText;
    private TextView windSpeedBtnText, courseBtnText, sejlforingBtnText,
            sejlStillingBtnText, rowersBtnText;

    private MyGPS gps;
    private String gpsData;

    private AppDatabase db;

    private final int WIND_FIELD = 0, ROWERS_FIELD = 1, SAILFORING_FIELD = 2, SAILDIRECTION_FIELD = 3, COURSE_FIELD = 4;
    private ImageView savedPicture;


    //AUDIO OG IMAGE VARIABLER:
    private static final String audioTAG = "TEST AF LYDOPTAGER";
    private static final String imageTAG = "TEST AF BILLEDEFUNKTION";

    private AudioRecorder audioRecorder;
    private AudioPlayer audioPlayer;

    private ProgressDialog progressDialog;

    private File audioFolder, imageFolder, imageFile;

    private String fileName;

    private boolean recordingDone, imageTaken;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_createnote);

        db = DatabaseBuilder.get(this);

        findViewById(R.id.createNoteAccepterBtn).setOnClickListener(this);

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

        commentText = findViewById(R.id.commentEditText);

        gps = new MyGPS(this);
        gpsData = "LAT: " + String.format(Locale.US, "%.2f", gps.getLocation().getLatitude()) + "\n" +
                "LON: " + String.format(Locale.US, "%.2f", gps.getLocation().getLongitude());
        System.out.println(gpsData);




        // Vigtigt at der her er noget, der aflæser om noten har et billede
        // gemt sammen med dens database objekt, således at det bliver muligt, at bestemme
        // om cameraButton skal være med "Kamera"-ikon eller bitmap af det gemte billede.
        cameraButton = findViewById(R.id.createNoteCameraBtn);
        cameraButton.setOnClickListener(this);

        savedPicture = findViewById(R.id.savedPicture);
        //Et eller andet med if(!imageTaken) så er det usynligt:
        savedPicture.setVisibility(View.INVISIBLE);

        // Vigtigt at der her er noget, der aflæser om noten har en lydoptagelse
        // gemt sammen med dens database objekt vha. recordingDone, således at det bliver muligt,
        // at bestemme om micButton skal være med "Play" eller "Mikrofon"-ikon.
        micButton = findViewById(R.id.createNoteMicBtn);
        micButton.setOnClickListener(this);
        if(recordingDone){
            //TODO ændre knap her til et nyt billede
            ((ImageView)findViewById(R.id.createNoteMic)).setImageResource(android.R.drawable.ic_media_play);
        }


        //Her skal der tjekkes for en instans af fileName inde i databasen, således at hvis der
        //allerede findes en fileName, skal denne bruges i stedet for at oprette et nyt, således
        //at det bliver muligt, at finde tidligere gemte billeder og lydfiler i telefonens hukommelse.
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss", Locale.getDefault());
        fileName = sdf.format(new Date());
        Log.d("Aktuelle filnavn: ", fileName);



        ActivityCompat.requestPermissions(this, permissions, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
        //Opretter mappen for lydnoter:
        audioFolder = new File(Environment.getExternalStorageDirectory() + "/Sejllog/Lydnoter/");
        if (!audioFolder.exists()) {
            try {
                audioFolder.mkdirs();
                audioFolder.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Opretter mappen for billeder:
        imageFolder = new File(Environment.getExternalStorageDirectory() + "/Sejllog/Billedenoter/");
        if (!imageFolder.exists()) {
            try {
                imageFolder.mkdirs();
                imageFolder.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setWindSpeed() {
        String[] s1 = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};

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
        int range = 20; //TODO should be crewsize
        String[] s = new String[range + 1];
        for (int n = 0; n < range + 1; n++) {
            s[n] = String.valueOf(n);
        }

        NoteDialog df = new NoteDialogNumberPicker(ROWERS_FIELD, s);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "rowers");
    }

    public void confirm() {

        SimpleDateFormat clock = new SimpleDateFormat("HH.mm", Locale.getDefault());
        String time = clock.format(new Date());

        Note note = new Note(getIntent().getLongExtra("etape_id", -1L), gpsData,
                windSpeed.getText().toString(),time,
                rowers.getText().toString(), sejlforing.getText().toString(), sejlStilling.getText().toString(), course.getText().toString(), commentText.getText().toString());

        Executors.newSingleThreadExecutor().execute(() -> {
            db.noteDAO().insert(note);
            setResult(Activity.RESULT_OK);
            finish();
        });
    }


    //Implementering af AudioRecorder, AudioPlayer og kamerafunktionalitet:
    @Override
    public void onClick(View v) {
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

            case R.id.createNoteAccepterBtn:
                confirm();
                break;
            case R.id.createNoteMicBtn:
                if (!recordingDone) {
                    ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                    audioRecorder = new AudioRecorder();
                    new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object... arg0) {
                            try {
                                audioRecorder.setupAudioRecord(audioFolder + "/" + fileName + ".mp3");
                                return Log.d(audioTAG, "Der gemmes en lydfil i: " + audioFolder + "/" + fileName + ".mp3");
                            } catch (Exception e) {
                                e.printStackTrace();
                                return Log.d(audioTAG, "Det virker IKKE: " + e);
                            }
                        }

                        @Override
                        protected void onPostExecute(Object obj) {
                            audioRecorder.startAudioRecord();
                        }
                    }.execute();

                    progressDialog = new ProgressDialog(CreateNote.this);
                    progressDialog.setMax(200);
                    progressDialog.setTitle("Optager lydnote...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Gem optagelse", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            audioRecorder.stopAudioRecord();
                            recordingDone = true;
                            //Skaber "PLAY"-knap.
                            ((ImageView) findViewById(R.id.createNoteMic)).setImageResource(android.R.drawable.ic_media_play);

                            Toast.makeText(CreateNote.this, "Lydnoten blev gemt i mappen: " + audioFolder, Toast.LENGTH_LONG).show();
                        }
                    });
                    progressDialog.show();

                } else if (recordingDone){
                    ActivityCompat.requestPermissions(this, permissions, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);

                    audioPlayer = new AudioPlayer();
                    new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object... arg0) {
                            try {
                                audioPlayer.setupAudioPlayer(audioFolder + "/" + fileName + ".mp3");
                                return Log.d(audioTAG, "Følgende lydfil afspilles: " + audioFolder + "/" + fileName + ".mp3");
                            } catch (Exception e){
                                e.printStackTrace();
                                return Log.d(audioTAG, "Det virker IKKE: " + audioFolder + "    " + fileName + e);
                            }}

                        @Override
                        protected void onPostExecute(Object obj){
                            audioPlayer.startAudioPlayer();
                        }
                    }.execute();

                    progressDialog = new ProgressDialog(CreateNote.this);
                    progressDialog.setMax(200);
                    progressDialog.setTitle("Afspiller lydnote...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Afslut afspilning", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            audioPlayer.stopAudioNote();
                        }});
                    progressDialog.show();
                }
                break;
            case R.id.createNoteCameraBtn:
                //Sender intent til at åbne kameraet og afventer resultatet.
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA_PERMISSION);

                //Ny fil der kan laves til en Uri længere nede:
                imageFile = new File(imageFolder + "/" + fileName + ".jpg");
                Uri imageURI = FileProvider.getUriForFile(this, "com.example.vikingesejllog.fileprovider", imageFile);

                //Udskriver stien til mig i terminalen:
                Log.d(imageTAG, imageFile.getAbsolutePath());

                //Intent der starter kameraet:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);}
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
            case REQUEST_READ_EXTERNAL_STORAGE_PERMISSION:
                if (!permissionToReadStorage){
                permissionToReadStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;}
            case REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (!permissionToWriteStorage){
                permissionToWriteStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;}
        }}

    //Først EFTER kameraet har kørt oprettes, der et BitmapFactory, der omdanner den gemte billedefil
    // til et bitmap, der kan vise en miniature udgave af billedet inde i noten:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Køres når der er et resultat fra kamera appen og gemmer det som et bitmap:
        super.onActivityResult(REQUEST_IMAGE_CAPTURE, resultCode, data);

        imageTaken = true; //Så vi kan tjekke med databasen senere!
        Toast.makeText(CreateNote.this, "Det originale billede blev gemt i mappen: " + imageFolder, Toast.LENGTH_LONG).show();

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.toString());
        savedPicture.setImageBitmap(bitmap);
        savedPicture.setRotation(90);
        savedPicture.setVisibility(View.VISIBLE);
        savedPicture.setOnTouchListener(this);
    }

    //Zoom ind på billede bitmap ved at røre det:
    @Override
    public boolean onTouch(View savedPicture, MotionEvent event) {
    /*Zoomer ind på billedet, hvis brugeren rør ved det, og zoomer ud igen,
    hvis fingeren slippes
     */
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                savedPicture.setElevation(100);
                savedPicture.setMinimumHeight(1000);
                savedPicture.setMinimumWidth(1000);
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                savedPicture.setElevation(1);
                return true;
            }
        return false;
    }

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
}
