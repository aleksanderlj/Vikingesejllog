package com.example.vikingesejllog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;



public class AudioRecorder extends AppCompatActivity {

    /*Author: s164497 - Hemsen

    Denne klasse har til formål at styre logikken bag lydoptagelsen inde i "Opret Note". Herved
    gemmes noten som en .MPEG4-fil. Filnavnet defineres ud fra hvilken note, der oprettes, således
    at hvert filnavn er unikt. Lige pt. gemmes de under music-mappen på SDkortet.
     */


    MediaRecorder audioRecorder;
    MediaPlayer audioPlayer;
    ProgressDialog progressDialog;

    final int REQUEST_PERMISSION_CODE = 1000;



    void recordAudio(String fileName) throws IOException {
        /*Denne metode gemmer en lydfil, der optages gennem mikrofonen. FileName skal vi have
        defineret som en variabel svarende til noten, der oprettes.
         */


        audioRecorder = new MediaRecorder();
        //fileName = et eller andet navn på noten

        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        audioRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/Download/" + fileName);

        audioRecorder.prepare();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(200);
        progressDialog.setTitle("Optager lydnote...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Gem optagelse", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                audioRecorder.stop();
                progressDialog.dismiss();
            }});

        audioRecorder.start();
        progressDialog.show();
    }

    private void stopAudioRecord(){
        //Stopper optagelsen og frigiver objektet til garbage-collector
    progressDialog.dismiss();
    audioRecorder.stop();
    audioRecorder.release();
}


    void playAudioNote(String fileName) throws IOException {

        //Husk fileName skal defineres enten her eller inden metoden kaldes.

        String filePath = Environment.getExternalStorageDirectory().getPath() + "/Download/" + fileName;

        audioPlayer = new MediaPlayer();
        audioPlayer.setDataSource(filePath);
        audioPlayer.setVolume(4, 4); //Så projektleder kan høre det
        audioPlayer.prepare();
        audioPlayer.start();

        progressDialog.setTitle("Afspiller lydnote");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        progressDialog.setButton(1, "Afslut afspilning", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopAudioNote();
            }
        });
    }

    private void stopAudioNote(){
        //Stopper afspilningen af noten.
        progressDialog.dismiss();
        audioPlayer.stop();
        audioPlayer.release();
    }

/*
    public boolean checkPermissionFromDevice(){
      int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
      int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
      return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
              record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
                break;
        }
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO

        }, REQUEST_PERMISSION_CODE);
    }

 */
}
