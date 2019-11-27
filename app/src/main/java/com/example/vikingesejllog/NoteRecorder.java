package com.example.vikingesejllog;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MicrophoneDirection;
import android.net.Uri;

import java.io.IOException;
import java.net.URI;

import static android.media.MediaRecorder.AudioEncoder.AAC;

public class NoteRecorder {

    /*Author: s164497 - Hemsen

    Denne klasse har til formål at styre logikken bag knaptrykkene på hhv. "Optag" og "Kamera",
    når brugeren er ved at lave en note. Herefter skal billedet eller lydfilen gemmes sammen med
    noten.

    Følgende skal tilføjes manifestet:
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />


    */
MediaRecorder mediaRecorder;
MediaPlayer audioPlayer, videoPlayer;
Uri test;

SharedPreferences sharedPreferences;

public void recordAudio() throws IOException {
    //Mangler noget der gemmet noten med samme navn som selve noten..

    mediaRecorder.setAudioSource(MicrophoneDirection.MIC_DIRECTION_TOWARDS_USER);
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
    mediaRecorder.setOutputFile(test.getPath());
    mediaRecorder.setAudioEncoder(AAC);
    mediaRecorder.prepare();
    mediaRecorder.start();
}

public void saveAudio(){
    mediaRecorder.stop();
    mediaRecorder.release();
}


public void playAudioNote() throws IOException {
    //Skal også have noget, der afspiller den rigtige lydfil afhængig af navn på note.

    audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    audioPlayer.setDataSource(getApplicationContext(), test);
    audioPlayer.setVolume(1, 1);
    audioPlayer.prepare();
    audioPlayer.start();
}

public void stopAudioNote(){
    audioPlayer.stop();
    audioPlayer.release();
}

public void saveImageNote(){

}

public void showImageNote(){

}
}
