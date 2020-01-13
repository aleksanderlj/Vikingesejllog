package com.example.vikingesejllog.note;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.note.dialogs.NoteDialog;
import com.example.vikingesejllog.note.dialogs.NoteDialogNumberPicker;
import com.example.vikingesejllog.note.dialogs.NoteDialogListener;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CreateNote extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, NoteDialogListener {

    // TODO ryd op i alle findByViewID() declarations (vi behøver ikke finde dem alle fra start)

    private TextView windSpeedBtnText;
    private TextView courseBtnText;
    private TextView sailingSpeedBtnText;
    private EditText sailingSpeed;
    private TextView pathBtnText;
    private TextView directionBtnText;
    private TextView rowersBtnText;
    private TextView timeText;
    private TextView gpsText;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_CAMERA_PERMISSION = 300;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 400;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 500;

    private static final String audioTAG = "TEST AF LYDOPTAGER";
    private static final String imageTAG = "TEST AF BILLEDEFUNKTION";

    private Button windSpeed, course, sejlforing, sejlStilling, rowers;
    private EditText commentText;
    private TextView windSpeedBtnText, courseBtnText, sejlforingBtnText,
            sejlStillingBtnText, rowersBtnText;

    private MyGPS gps;

    private Button micButton, cameraButton;
    private ImageView savedPicture;

    //Media support:
    private AudioRecorder audioRecorder;
    private AudioPlayer audioPlayer;

    private Intent takePictureIntent;

    private File audioFolder, imageFolder;

    private String fileName;

    private boolean recordingDone;

    private AppDatabase db;
    // Permissions support:
    private boolean permissionToRecordAccepted;
    private boolean permissionToCamera;
    private boolean permissionToReadStorage;
    private boolean permissionToWriteStorage;
    private String s;

    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private final int STORAGE_PERMISSION_CODE = 1;
    private final int WIND_FIELD = 0, ROWERS_FIELD = 1, SAILFORING_FIELD = 2, SAILDIRECTION_FIELD = 3, COURSE_FIELD = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_createnote);

        db = DatabaseBuilder.get(this);

        windSpeedBtnText = findViewById(R.id.windSpeedBtnText);
        windSpeed = findViewById(R.id.windspeedBtn);

        course = findViewById(R.id.courseBtn);
        courseBtnText = findViewById(R.id.courseBtnText);

        sejlforing = findViewById(R.id.sailforingBtn);
        sejlforingBtnText = findViewById(R.id.sejlfoeringBtnText);

        sejlStilling = findViewById(R.id.sailStillingBtn);
        sejlStillingBtnText = findViewById(R.id.sejlstillingBtnText);

        rowers = findViewById(R.id.rowerCountBtn);
        rowersBtnText = findViewById(R.id.rowersBtnText);

        commentText = findViewById(R.id.commentEditText);

        micButton = findViewById(R.id.createNoteMicBtn);
        micButton.setOnClickListener(this);
        audioRecorder = new AudioRecorder();

        cameraButton = findViewById(R.id.createNoteCameraBtn);
        cameraButton.setOnClickListener(this);

        savedPicture = findViewById(R.id.savedPicture);
        savedPicture.setVisibility(View.INVISIBLE);

        micButton = findViewById(R.id.createNoteMicBtn);
        if(recordingDone){
            //TODO ændre knap her til et nyt billede
            // micButton.setImageResource(android.R.drawable.ic_media_play);
        }

        cameraButton.setOnClickListener(this);
        micButton.setOnClickListener(this);

        savedPicture.setImageAlpha(0);


        try {
            gps = new MyGPS(this);
             s = "LAT: " + String.format(Locale.US, "%.2f", gps.getLocation().getLatitude()) + "\n" +
                    "LON: " + String.format(Locale.US, "%.2f", gps.getLocation().getLongitude());
        }
        catch(Exception e) {
            e.printStackTrace();
            s = "";
        }



        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.HH.mm", Locale.getDefault());
        fileName = sdf.format(new Date());
        Log.d("Aktuelle filnavn: ", fileName);



        ActivityCompat.requestPermissions(this, permissions, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
        //Opretter mappen for lydnoter:
        audioFolder = new File("/sdcard/Download/" + "Lydnoter");
        if (!audioFolder.exists()) {
            try {
                audioFolder.mkdirs();
                audioFolder.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Opretter mappen for billeder:
        imageFolder = new File("/sdcard/Download/" + "Billedenoter");
        if (!imageFolder.exists()) {
            try {
                imageFolder.mkdirs();
                imageFolder.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setWindSpeed(final View v) {
        String[] s1 = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};

        int range = 33;
        String[] s2 = new String[range + 1];
        for (int n = 0; n < range; n++) {
            s2[n] = String.valueOf(n) + " m/s";
        }
        s2[s2.length - 1] = "33+";


        // TODO Direction skal gerne vælges fra et kompas, så skal nok lave en anden klasse
        NoteDialog df = new NoteDialogNumberPicker(WIND_FIELD, s1, s2);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "wind");
    }

    public void setCourse(final View v) {
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

//    public void setSailingSpeed(final View v) {
//
//        sailingSpeedBtnText.setVisibility(View.INVISIBLE);
//
//        sailingSpeed.setVisibility(View.VISIBLE);
//
//        sailingSpeed.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//
//        sailingSpeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (!hasFocus) {
//
//                    sailingSpeedBtnText.setText(sailingSpeed.getText() + " kn");
//                    sailingSpeedBtnText.setVisibility(View.VISIBLE);
//                    sailingSpeed.setVisibility(View.INVISIBLE);
//
//                }
//            }
//        });
//    }

        sailingSpeedBtnText.setVisibility(View.INVISIBLE);

        sailingSpeed.setVisibility(View.VISIBLE);

        sailingSpeed.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        sailingSpeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    sailingSpeedBtnText.setText(sailingSpeed.getText() + " kn");
                    sailingSpeedBtnText.setVisibility(View.VISIBLE);
                    sailingSpeed.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setPath(final View v) {
        String[] s = {"F", "Ø", "N1", "N2", "N3"};

        NoteDialog df = new NoteDialogNumberPicker(SAILFORING_FIELD, s);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "sailforing");
    }

    public void setDirection(final View v) {
        String[] s1 = {"bi", "fo", "ha", "ag", "læ"};
        String[] s2 = {"sb", "bb"};

        NoteDialog df = new NoteDialogNumberPicker(SAILDIRECTION_FIELD, s1, s2);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "saildirection");
    }

    public void setRowers(final View v) {
        int range = 20; //TODO should be crewsize
        String[] s = new String[range + 1];
        for (int n = 0; n < range + 1; n++) {
            s[n] = String.valueOf(n);
        }

        NoteDialog df = new NoteDialogNumberPicker(ROWERS_FIELD, s);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "rowers");
    }

    public void confirm(View v) {

        SimpleDateFormat clock = new SimpleDateFormat("HH.mm", Locale.getDefault());
        String time = clock.format(new Date());

        Note note = new Note(getIntent().getLongExtra("etape_id", -1L), s,
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
        ProgressDialog progressDialog;
        if (v == micButton && !recordingDone) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

                audioRecorder = new AudioRecorder();

                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object... arg0) {
                        try {
                            audioRecorder.setupAudioRecord(audioFolder + "/" + fileName + ".mp3");
                            return Log.d(audioTAG, "Der gemmes en lydfil i: " + audioFolder + "/" + fileName + ".mp3");
                        } catch (Exception e){
                            e.printStackTrace();
                            return Log.d(audioTAG, "Det virker IKKE: " + e);
                        } }

                    @Override
                    protected void onPostExecute(Object obj){
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
                    }});
                progressDialog.show();

                //Skaber "PLAY"-knap.
                //TODO ændre mic button til noget nyt
                //micButton.setImageResource(android.R.drawable.ic_media_play);
        }


        if (v == micButton && recordingDone){
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


        if (v == cameraButton){
            //Sender intent til at åbne kameraet og afventer resultatet.
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA_PERMISSION);

            takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageFolder);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            /*new AsyncTask() {
                @Override
                protected Object doInBackground(Object... arg0) {
                    try {
                        image = File.createTempFile(fileName, ".jpg", imageFolder);
                        return Log.d(imageTAG, image.toString());
                    } catch (Exception e){
                        e.printStackTrace();
                        return Log.d(imageTAG, "Det virker IKKE: " + image + e);
                    }}

                @Override
                protected void onPostExecute(Object obj){
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageFolder);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }}
            }.execute();*/
            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFolder + "/" + fileName + ".png");
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


    //Zoom ind på billede bitmap ved at røre det:
    @Override
    public boolean onTouch(View takenPicture, MotionEvent event) {
    /*Zoomer ind på billedet, hvis brugeren rør ved det, og zoomer ud igen,
    hvis fingeren slippes
     */
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                savedPicture.setImageAlpha(255);
                savedPicture.setElevation(100);
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                savedPicture.setImageAlpha(0);
                return true;
            }
        return false;
    }
}
