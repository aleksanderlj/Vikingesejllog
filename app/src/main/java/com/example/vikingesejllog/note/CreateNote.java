package com.example.vikingesejllog.note;

import android.Manifest;
import android.app.Activity;
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
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CreateNote extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {


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

    public void setWindSpeed(final View v){

        windSpeedBtnText.setVisibility(View.INVISIBLE);

        windSpeed.setVisibility(View.VISIBLE);

        windSpeed.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        windSpeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    windSpeedBtnText.setText(windSpeed.getText() + " m/s");
                    windSpeedBtnText.setVisibility(View.VISIBLE);
                    v.setVisibility(View.VISIBLE);
                    windSpeed.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setCourse(final View v) {

        courseBtnText.setVisibility(View.INVISIBLE);

        course.setVisibility(View.VISIBLE);

        course.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        course.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    courseBtnText.setText(course.getText());
                    courseBtnText.setVisibility(View.VISIBLE);
                    v.setVisibility(View.VISIBLE);
                    course.setVisibility(View.INVISIBLE);

                }
            }
        });
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

    public void setSejlforing(final View v) {

        sejlforingBtnText.setVisibility(View.INVISIBLE);

        sejlforing.setVisibility(View.VISIBLE);

        sejlforing.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        sejlforing.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    sejlforingBtnText.setText(sejlforing.getText());
                    sejlforingBtnText.setVisibility(View.VISIBLE);
                    sejlforing.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setSejlStilling(final View v) {

        sejlStillingBtnText.setVisibility(View.INVISIBLE);

        sejlStilling.setVisibility(View.VISIBLE);

        sejlStilling.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        sejlStilling.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    sejlStillingBtnText.setText(sejlStilling.getText());
                    sejlStillingBtnText.setVisibility(View.VISIBLE);
                    sejlStilling.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setRowers(final View v) {

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
