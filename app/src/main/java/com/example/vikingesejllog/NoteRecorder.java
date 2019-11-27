package com.example.vikingesejllog;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MicrophoneDirection;

import java.io.IOException;

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

SharedPreferences sharedPreferences;

public void recordAudio() throws IOException {
    mediaRecorder.setAudioSource(MicrophoneDirection.MIC_DIRECTION_TOWARDS_USER);
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
    mediaRecorder.setOutputFile("LydNote");
    mediaRecorder.setAudioEncoder(AAC);
    mediaRecorder.prepare();
    mediaRecorder.start();
}

public void saveAudio(){
    mediaRecorder.stop();
    mediaRecorder.release();
}


public void playAudioNote() {
    audioPlayer = MediaPlayer.create(this, mediaRecorder.);
    audioPlayer.setVolume(1, 1);

    setVolumeControlStream(AudioManager.STREAM_MUSIC);

}




}
