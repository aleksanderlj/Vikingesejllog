package com.example.vikingesejllog.note;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.sentry.Sentry;

public class AudioPlayer extends AppCompatActivity {
    /*Denne klasse har til formål at styre logikken bag lydafspilning inde i "MakeNoteActivity". Herved
    kan optagelsen, der er knyttet til den specifikke note afspilles igen.
    Filnavnet defineres ud fra hvilken note, der oprettes, således
    at hvert filnavn er unikt baseret på dato og tid.*/

    MediaPlayer audioPlayer;


    public void setupAudioPlayer(String fileDestination) {
        //Gør afspilleren klar samt input af fildestination
        try {
            audioPlayer = new MediaPlayer();
            audioPlayer.setDataSource(fileDestination);
            audioPlayer.setVolume(100,100);
            audioPlayer.prepare();
        } catch (IOException e) {
            Log.d("INDLÆSNINGSFEJL", "Filen blev ikke indlæst" + fileDestination);
            Sentry.capture(e);
            e.printStackTrace();
        }}


    public void startAudioPlayer(){
        //Starter afspilning
        audioPlayer.start();
    }


    public void rewindAudioPlayer(){//Gør audioPlayer klar til at spille igen
        audioPlayer.pause();
        audioPlayer.seekTo(0);
    }

    public void resetAudioPlayer(){
        audioPlayer.stop();
        audioPlayer.reset();
    }


    public boolean isAudioPlaying(){
        return audioPlayer.isPlaying();
    }


    public String returnDurationString(){
        //Formaterer længden på optagelsen:
        int audioDurationMilliseconds = audioPlayer.getDuration();
        String audioDuration = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(audioDurationMilliseconds),
                TimeUnit.MILLISECONDS.toSeconds(audioDurationMilliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(audioDurationMilliseconds)));
        return audioDuration;
    }


    public void endAudioPlayer(){
        if (audioPlayer != null) {
            audioPlayer.stop();
            audioPlayer.reset();
            audioPlayer.release();
            audioPlayer = null;
        }
    }
}
