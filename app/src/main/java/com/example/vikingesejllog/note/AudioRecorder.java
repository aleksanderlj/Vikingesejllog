package com.example.vikingesejllog.note;

import android.media.MediaRecorder;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import io.sentry.Sentry;


public class AudioRecorder extends AppCompatActivity {

    /*Denne klasse har til formål at styre logikken bag lydoptagelsen inde i "MakeNoteActivity". Herved
    gemmes noten som en .3gp fil. Filnavnet defineres ud fra hvilken note, der oprettes, således
    at hvert filnavn er unikt baseret på dato og tid.*/


    MediaRecorder audioRecorder;


    public void setupAudioRecord(String fileName) throws IOException {

        //fileName = et eller andet unikt navn på noten - lige nu filePath + fileName
        try {
            audioRecorder = new MediaRecorder();
            audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            audioRecorder.setOutputFile(fileName);
            audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            audioRecorder.prepare();
        }catch (IOException e) {
            Sentry.capture(e);
            e.printStackTrace();
        }}

    public void startAudioRecord(){
        audioRecorder.start();
    }



    public void stopAudioRecord(){
        //Stopper optagelsen og frigiver objektet til garbage-collector
    audioRecorder.stop();
    audioRecorder.release();
    audioRecorder = null;
    }
}
