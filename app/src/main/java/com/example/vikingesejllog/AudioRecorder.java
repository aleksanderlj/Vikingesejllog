package com.example.vikingesejllog;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;



public class AudioRecorder extends AppCompatActivity {

    /*Author: s164497 - Hemsen

    Denne klasse har til formål at styre logikken bag lydoptagelsen inde i "Opret Note". Herved
    gemmes noten som en .MPEG4-fil. Filnavnet defineres ud fra hvilken note, der oprettes, således
    at hvert filnavn er unikt. Lige pt. gemmes de under music-mappen på SDkortet.
     */


    MediaRecorder audioRecorder;


    void setupAudioRecord(String fileName) throws IOException {
        /*Denne metode gemmer en lydfil, der optages gennem mikrofonen. FileName skal vi have
        defineret som en variabel svarende til noten, der oprettes.
         */
        //fileName = et eller andet unikt navn på noten f.eks. datoen
        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        audioRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + fileName);
        try {
            audioRecorder.prepare();
        }catch (IOException e) {
            Log.d("Fejl", "Lortet virker ikke FEJL");
        }
        audioRecorder.start();
    }

    void stopAudioRecord(){
        //Stopper optagelsen og frigiver objektet til garbage-collector
    audioRecorder.stop();
    audioRecorder.release();
    audioRecorder = null;
    }
}
