package com.example.vikingesejllog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Button record, play, snapshot, test;
    ImageView imageNote;
    AudioRecorder audioRecorder;
    ProgressDialog progressDialog;

    /*Author: s164497 - Hemsen :-)

    Følgende skal tilføjes manifestet:
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />


    <uses-feature android:name="android.hardware.camera"
                  android:required="true" />



    Ser ingen grund til at have en klasse til ImageRecorder, da det bare kan gøres med et intent
    og et bitmap lige pt:
    */

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

        snapshot = new Button(this);
        snapshot.setText("Tag billede");
        snapshot.setOnClickListener(this);
        tl.addView(snapshot);

        imageNote = new ImageView(this);
        imageNote.setImageResource(R.drawable.logo);
        imageNote.setOnTouchListener(this);
        tl.addView(imageNote);

        test = new Button(this);
        test.setText("Test");
        test.setOnClickListener(this);
        tl.addView(test);

        setContentView(tl);
    }

    @Override
    public void onClick(View v) {
        //Skal have defineret en variabel for fileName!!!
        if (v == record) {
            try {
                audioRecorder.recordAudio("test");
            } catch (IOException e) {
                e.printStackTrace();
            } }

        if (v == play){
        try {
            audioRecorder.playAudioNote("test");
        } catch (IOException e) {
            e.printStackTrace();
        }}

        if (v == snapshot){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, 0);
        }

        if (v == test){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMax(200);
            progressDialog.setTitle("Optager lydnote...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Gem optagelse", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressDialog.dismiss();
                }});

            progressDialog.show();
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
    public boolean onTouch(View imageNote, MotionEvent event) {
        /*Zoomer ind på billedet, hvis brugeren rør ved det, og zoomer ud igen,
        hvis fingeren slippes
         */

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            imageNote.setMinimumHeight(2000);
            imageNote.setMinimumWidth(2000);
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            imageNote.setMinimumHeight(200);
            imageNote.setMinimumWidth(200);
            return true;
        }
        return false;
    }
}
