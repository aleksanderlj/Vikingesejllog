package com.example.vikingesejllog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class MakeNoteActivity extends AppCompatActivity {

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
        gps = new MyGPS(this);
        gpsText.setText("LAT: "+(gps.getLocation().getLatitude()+"\n"+"LON: "+(String.valueOf(gps.getLocation().getLongitude()).substring(0,8))));
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
        result.putExtra("myjson",myJson);
        setResult(MainActivity.RESULT_OK,result);

        finish();
    }
}