package com.example.vikingesejllog.note;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.vikingesejllog.MainActivity;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Note;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNote extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{


    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_CAMERA_PERMISSION = 300;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 400;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 500;

    private static final String TAG = "TEST AF LYDOPTAGER";



    private EditText windSpeed, course, sailingSpeed, path, direction, rowers, commentText;
    private TextView windSpeedBtnText, courseBtnText, sailingSpeedBtnText, pathBtnText,
            directionBtnText, rowersBtnText, timeText, gpsText;

    private MyGPS gps;
    private MyTime time;

    private ImageButton micButton, cameraButton;
    private ImageView takenPicture, savedPicture;

    private AudioRecorder audioRecorder;
    private AudioPlayer audioPlayer;
    private String audioFolderName = "Lydnoter";
    private String imageFolderName = "Billednoter";
    private File audioFolder, imageFolder;

    private String fileName;

    private boolean recordingDone = true;

    // Boolean, der checker for permissions.
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToCamera = false;
    private boolean permissionToReadStorage = false;
    private boolean permissionToWriteStorage= false;

    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_createnote);

        windSpeed = findViewById(R.id.windspeedText);
        windSpeedBtnText = findViewById(R.id.windspeedButtonText);
        course = findViewById(R.id.courseText);
        courseBtnText = findViewById(R.id.courseButtonText);
        sailingSpeed = findViewById(R.id.sailingSpeed);
        sailingSpeedBtnText = findViewById(R.id.sailingSpeedButtonText);
        path = findViewById(R.id.pathText);
        pathBtnText = findViewById(R.id.pathButtonText);
        direction = findViewById(R.id.directionText);
        directionBtnText = findViewById(R.id.directionButtonText);
        rowers = findViewById(R.id.rowers);
        rowersBtnText = findViewById(R.id.rowersButtonText);
        timeText = findViewById(R.id.clockButtonText);
        gpsText = findViewById(R.id.coordsButtonText);
        commentText = findViewById(R.id.textComment);


        ActivityCompat.requestPermissions(this, permissions, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);

        audioFolder = new File(Environment.DIRECTORY_MUSIC, audioFolderName);
        if (!audioFolder.exists()) {
            audioFolder.mkdirs();
        }

        imageFolder = new File(Environment.DIRECTORY_PICTURES, imageFolderName);
        if (!imageFolder.exists()){
            imageFolder.mkdirs();
        }
        /*Skaffer lagerlokation til mig:
        filePath = getExternalCacheDir().getAbsolutePath();*/
        Log.d(TAG, audioFolder.toString() + imageFolder.toString());

        micButton = findViewById(R.id.micButton);
        if(recordingDone){
            micButton.setImageResource(android.R.drawable.ic_media_play);
        }


        cameraButton= findViewById(R.id.cameraButton);
        takenPicture = findViewById(R.id.takenPicture);
        savedPicture = findViewById(R.id.savedPicture);

        takenPicture.setOnTouchListener(this);

        cameraButton.setOnClickListener(this);
        micButton.setOnClickListener(this);

        savedPicture.setImageAlpha(0);

        gps = new MyGPS(this);
        String s = "LAT: "+(gps.getLocation().getLatitude()+"\n"+"LON: "+(String.valueOf(gps.getLocation().getLongitude()).substring(0,14)));
        gpsText.setText(s);

        time = new MyTime();
        timeText.setText(time.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-HH:mm", Locale.getDefault());
        fileName = sdf.format(new Date());
        Log.d(TAG, fileName);
    }

    public void setWindSpeed(final View v){

        windSpeedBtnText.setVisibility(View.INVISIBLE);

        windSpeed.setVisibility(View.VISIBLE);

        windSpeed.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        windSpeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    windSpeedBtnText.setText(windSpeed.getText()+" m/s");
                    windSpeedBtnText.setVisibility(View.VISIBLE);
                    v.setVisibility(View.VISIBLE);
                    windSpeed.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setCourse(final View v){

        courseBtnText.setVisibility(View.INVISIBLE);

        course.setVisibility(View.VISIBLE);

        course.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

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

    public void setSailingSpeed(final View v){

        sailingSpeedBtnText.setVisibility(View.INVISIBLE);

        sailingSpeed.setVisibility(View.VISIBLE);

        sailingSpeed.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        sailingSpeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    sailingSpeedBtnText.setText(sailingSpeed.getText()+" kn");
                    sailingSpeedBtnText.setVisibility(View.VISIBLE);
                    sailingSpeed.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setPath(final View v){

        pathBtnText.setVisibility(View.INVISIBLE);

        path.setVisibility(View.VISIBLE);

        path.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        path.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    pathBtnText.setText(path.getText());
                    pathBtnText.setVisibility(View.VISIBLE);
                    path.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setDirection(final View v){

        directionBtnText.setVisibility(View.INVISIBLE);

        direction.setVisibility(View.VISIBLE);

        direction.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        direction.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    directionBtnText.setText(direction.getText());
                    directionBtnText.setVisibility(View.VISIBLE);
                    direction.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setRowers(final View v) {

        //TODO Måske ændre PopupMenu til et PopupWindow som ser lidt pænere ud

        PopupMenu popup = new PopupMenu(this, v);

        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                rowersBtnText.setText(item.getTitle());
                rowersBtnText.setVisibility(View.VISIBLE);
                rowers.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        popup.show();
    }

    public void confirm(View v){

        Note note = new Note(gps.getLocation().getLatitude()+String.valueOf(gps.getLocation().getLongitude()).substring(0,8),
                                    sailingSpeed.getText().toString(), windSpeed.getText().toString(), timeText.getText().toString(),
                                    rowers.getText().toString(), path.getText().toString(), direction.getText().toString(),course.getText().toString(),commentText.getText().toString());

        Gson gson = new Gson();
        String myJson = gson.toJson(note);

        Intent result = new Intent();
        result.putExtra("note",myJson);
        setResult(MainActivity.RESULT_OK,result);

        finish();
    }

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
                            return Log.d(TAG, "Det virker: " + audioFolder + "/" + fileName + ".mp3");
                        } catch (Exception e){
                            e.printStackTrace();
                            return Log.d(TAG, "Det virker IKKE: " + e);
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
                micButton.setImageResource(android.R.drawable.ic_media_play);
        }


        if (v == micButton && recordingDone){
                ActivityCompat.requestPermissions(this, permissions, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);

                audioPlayer = new AudioPlayer();

                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object... arg0) {
                        try {
                            audioPlayer.setupAudioNote(audioFolder + "/" + fileName + ".mp3");
                            return Log.d(TAG, "Det virker: " + audioFolder + "/" + fileName + ".mp3");
                        } catch (Exception e){
                            e.printStackTrace();
                            return Log.d(TAG, "Det virker IKKE: " + e);
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

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFolder + "/" + fileName + ".png");
            startActivityForResult(takePictureIntent, 1);
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

    //Gemmer billede som bitmap:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Køres når der er et resultat fra kamera appen og gemmer det som et bitmap:
        super.onActivityResult(1, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        //Skal evt. gemmes i noget sharedprefs med navn på note..
        takenPicture.setImageBitmap(bitmap);
        savedPicture.setImageBitmap(bitmap);

    }

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
