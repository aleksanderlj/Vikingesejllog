package com.example.vikingesejllog;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AudioPlayer extends AppCompatActivity {
     /*Author: s164497 - Hemsen

    Denne klasse har til formål at styre logikken bag lydoptagelsen inde i "Opret Note". Herved
    gemmes noten som en .MPEG4-fil. Filnavnet defineres ud fra hvilken note, der oprettes, således
    at hvert filnavn er unikt. Lige pt. gemmes de under music-mappen på SDkortet.
     */

    MediaPlayer audioPlayer;


    void playAudioNote() throws IOException {
        //Husk fileName skal defineres enten her eller inden metoden kaldes.

        //String filePath = Environment.getExternalStorageDirectory().getPath()+ fileName;

        audioPlayer = MediaPlayer.create(this, R.raw.toilet_flushing);
        audioPlayer.setVolume(5, 5); //Så projektleder kan høre det

        try {
            audioPlayer.prepare();
            audioPlayer.start();
        } catch (IOException e) {
            Log.d("Fejl", "onClick: FEJL");
        }
    }

    void stopAudioNote(){
        //Stopper afspilningen af noten.
        //progressDialog.dismiss();
        audioPlayer.stop();
        audioPlayer.release();
        audioPlayer = null;
    }
}
