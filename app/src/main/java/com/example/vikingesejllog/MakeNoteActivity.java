package com.example.vikingesejllog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vikingesejllog.model.Note;
import com.google.gson.Gson;

import java.io.IOException;

public class MakeNoteActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{

    private MyGPS gps;
    private EditText windSpeed;
    private TextView windSpeedBtnText;
    private EditText course;
    private TextView courseBtnText;
    private TextView sailingSpeedBtnText;
    private EditText sailingSpeed;
    private EditText path;
    private TextView pathBtnText;
    private EditText direction;
    private TextView directionBtnText;
    private EditText rowers;
    private TextView rowersBtnText;
    private TextView timeText;
    private TextView gpsText;
    private EditText commentText;

    private ImageButton micButton;
    private ImageButton cameraButton;
    private ImageView takenPicture, savedPicture;

    AudioRecorder audioRecorder;
    private boolean recordingDone;


    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makenote);

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
        gps = new MyGPS(this);


        micButton = findViewById(R.id.micButton);
        micButton.setOnClickListener(this);
        audioRecorder = new AudioRecorder();

        cameraButton= findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(this);

        takenPicture = findViewById(R.id.takenPicture);
        takenPicture.setOnTouchListener(this);

        savedPicture = findViewById(R.id.savedPicture);
        savedPicture.setImageAlpha(0);


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

    public void setRowers(final View v){

        rowersBtnText.setVisibility(View.INVISIBLE);

        rowers.setVisibility(View.VISIBLE);

        rowers.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        rowers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    rowersBtnText.setText(rowers.getText());
                    rowersBtnText.setVisibility(View.VISIBLE);
                    rowers.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setCoordinates(View v){
        String s = "LAT: "+(gps.getLocation().getLatitude()+"\n"+"LON: "+(String.valueOf(gps.getLocation().getLongitude()).substring(0,8)));
        gpsText.setText(s);
    }

    public void setTime(View v){
        MyTime time = new MyTime();
        timeText.setText(time.getTime());

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
            if (v == micButton && !recordingDone) {
                try {
                        audioRecorder.recordAudio("test");
                        micButton.setImageResource(R.drawable.nem);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            if (v == micButton && recordingDone){
                //Skal køres først, for at sikre, at brugeren har givet tilladelse til appen.
                if (ContextCompat.checkSelfPermission(MakeNoteActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    try {
                        audioRecorder.playAudioNote("test");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Hvis tilladelsen ikke allerede er givet, skal der spørges efter den.
                    requestStoragePermission(); }
            }
            if (v == cameraButton){
                //Sender intent til at åbne kameraet og afventer resultatet.
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, 0);
            }
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            //Køres når der er et resultat fra kamera appen og gemmer det som et bitmap:
            super.onActivityResult(requestCode, resultCode, data);
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            //Skal evt. gemmes i noget sharedprefs med navn på note..
            takenPicture.setImageBitmap(bitmap);
            savedPicture.setImageBitmap(bitmap);
        }

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


        //Følgede to metoder beder brugeren om tilladelse til at tilgå enhedens lagerplads:
    public void requestStoragePermission() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

                new AlertDialog.Builder(this)
                        .setTitle("Tilladelse påkrævet!")
                        .setMessage("Følgende tilladelsen kræves for at kunne afspille gemte lydnoter")
                        .setPositiveButton("Godkend", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                {ActivityCompat.requestPermissions(MakeNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                                }
                            }})
                        .setNegativeButton("Afvis", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();

            }   else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == STORAGE_PERMISSION_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Tilladelsen blev givet", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(this, "Tilladelsen blev afvist", Toast.LENGTH_SHORT).show();
                }
            }
        }

}
