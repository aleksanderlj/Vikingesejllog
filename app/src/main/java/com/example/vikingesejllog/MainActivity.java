package com.example.vikingesejllog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button record, play;
    AudioRecorder audioRecorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioRecorder = new AudioRecorder();

        TableLayout tl = new TableLayout(this);

        record = new Button(this);
        record.setText("Optag lyd");
        record.setOnClickListener(this);
        tl.addView(record);

        play = new Button(this);
        play.setText("Afspil lyd");
        play.setOnClickListener(this);
        tl.addView(play);

        setContentView(tl);
    }

    @Override
    public void onClick(View v) {
        if (v == record) {
            try {
                audioRecorder.recordAudio("Noten");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            audioRecorder.playAudioNote("Noten");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
