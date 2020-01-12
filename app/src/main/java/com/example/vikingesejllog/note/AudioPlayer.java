package com.example.vikingesejllog.note;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AudioPlayer extends AppCompatActivity {
     /*Author: s164497 - Hemsen

    Denne klasse har til formål at styre logikken bag lydafspilning inde i "MakeNoteActivity". Herved
    kan optagelsen, der er knyttet til den specifikke note afspilles igen.
    Filnavnet defineres ud fra hvilken note, der oprettes, således
    at hvert filnavn er unikt. Der mangler databasefunktionalitet!
     */

    MediaPlayer audioPlayer;


    public void setupAudioPlayer(String fileDestination) {
        //Gør afspilleren klar samt input af fildestination
        try {
            audioPlayer = new MediaPlayer();
            audioPlayer.setDataSource(fileDestination);
            audioPlayer.setVolume(75,75);
            audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            audioPlayer.prepare();
        } catch (IOException e) {
            Log.d("INDLÆSNINGSFEJL", "Filen blev ikke indlæst" + fileDestination);
            e.printStackTrace();
        }}

    public void startAudioPlayer(){
        //Starter afspilning
        audioPlayer.start();
    }

    public void stopAudioNote(){
        //Stopper afspilningen af noten og frigiver objekt
        audioPlayer.stop();
        audioPlayer.reset();
        audioPlayer.release();
        audioPlayer = null;
    }
}
