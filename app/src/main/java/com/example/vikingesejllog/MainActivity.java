package com.example.vikingesejllog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Button record, play, snapshot;
    ImageView imageNote;
    AudioRecorder audioRecorder;
    ImageRecorder imageRecorder;

    /*Author: s164497 - Hemsen

    Følgende skal tilføjes manifestet:
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />


    <uses-feature android:name="android.hardware.camera"
                  android:required="true" />

    <provider
        android:name="android.support.v4.content.FileProvider"
        android:authorities="com.example.android.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths"></meta-data>
    </provider>
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioRecorder = new AudioRecorder();
        imageRecorder = new ImageRecorder();

        TableLayout tl = new TableLayout(this);

        record = new Button(this);
        record.setText("Optag lyd");
        record.setOnClickListener(this);
        tl.addView(record);

        play = new Button(this);
        play.setText("Afspil lyd");
        play.setOnClickListener(this);
        tl.addView(play);

        snapshot = new Button(this);
        snapshot.setText("Tag billede");
        snapshot.setOnClickListener(this);
        tl.addView(snapshot);

        imageNote = new ImageView(this);
        imageNote.setImageResource(R.drawable.logo);
        imageNote.setOnTouchListener(this);
        tl.addView(imageNote);


        setContentView(tl);
    }

    @Override
    public void onClick(View v) {
        if (v == record) {
            try {
                audioRecorder.recordAudio("Noten");
            } catch (IOException e) {
                e.printStackTrace();
            } }

        if (v == play){
        try {
            audioRecorder.playAudioNote("Noten");
        } catch (IOException e) {
            e.printStackTrace();
        }}

        if (v == snapshot){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, 0);
        }

        if (v == imageNote) {
            imageNote.setMinimumHeight(2000);
            imageNote.setMinimumWidth(2000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        //Skal evt. gemmes i noget sharedprefs med navn på note..
        imageNote.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == imageNote) {
            imageNote.setMinimumHeight(2000);
            imageNote.setMinimumWidth(2000);
        }
        return false;
    }
}
