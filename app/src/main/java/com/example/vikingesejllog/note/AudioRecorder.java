package com.example.vikingesejllog;

import android.media.MediaRecorder;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;



public class AudioRecorder extends AppCompatActivity {

    /*Author: s164497 - Hemsen

    Denne klasse har til formål at styre logikken bag lydoptagelsen inde i "MakeNoteActivity". Herved
    gemmes noten som en .3gp fil. Filnavnet defineres ud fra hvilken note, der oprettes, således
    at hvert filnavn er unikt. Der mangler databasefunktionalitet!
     */


    MediaRecorder audioRecorder;


    void setupAudioRecord(String fileDestination) throws IOException {

        //fileDestination = et eller andet unikt navn på noten - lige nu filePath + fileName
        try {
            audioRecorder = new MediaRecorder();
            audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            audioRecorder.setOutputFile(fileDestination);
            audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            audioRecorder.prepare();
        }catch (IOException e) {
            e.printStackTrace();
        }}

    void startAudioRecord(){
        audioRecorder.start();
    }

    void stopAudioRecord(){
        //Stopper optagelsen og frigiver objektet til garbage-collector
    audioRecorder.stop();
    audioRecorder.release();
    audioRecorder = null;
    }
}
