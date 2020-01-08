package com.example.vikingesejllog;

import android.media.AudioManager;
import android.media.MediaPlayer;
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

    void setupAudioNote(String fileDestination) throws IOException {
        //Gør afspilleren klar samt input af fildestination
        try {
            audioPlayer = new MediaPlayer();
            audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            audioPlayer.setVolume(5,5);
            audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            audioPlayer.setVolume(5,5);
            audioPlayer.setDataSource(fileDestination);
            audioPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }}

    void startAudioPlayer(){
        //Starter afspilning
        audioPlayer.start();
    }

    void stopAudioNote(){
        //Stopper afspilningen af noten og frigiver objekt
        audioPlayer.stop();
        audioPlayer.release();
        audioPlayer = null;
    }
}
