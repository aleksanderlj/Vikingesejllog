package com.example.vikingesejllog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
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


    void recordAudio(String fileName) throws IOException {
        /*Denne metode gemmer en lydfil, der optages gennem mikrofonen. FileName skal vi have
        defineret som en variabel svarende til noten, der oprettes.
         */


        audioRecorder = new MediaRecorder();
        //fileName = et eller andet navn på noten

        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        audioRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + fileName);

        audioRecorder.prepare();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(200);
        progressDialog.setTitle("Optager lydnote...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Gem optagelse", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopAudioRecord();
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

        String filePath = Environment.getExternalStorageDirectory().getPath()+ fileName;

        audioPlayer = new MediaPlayer();
        audioPlayer.setDataSource(filePath);
        audioPlayer.setVolume(4, 4); //Så projektleder kan høre det
        audioPlayer.prepare();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(200);
        progressDialog.setTitle("Afspiller lydnote");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Afslut afspilning", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopAudioNote();


            }});

        progressDialog.show();
        audioPlayer.start();
    }

    private void stopAudioNote(){
        //Stopper afspilningen af noten.
        progressDialog.dismiss();
        audioPlayer.stop();
        audioPlayer.release();
    }
}
