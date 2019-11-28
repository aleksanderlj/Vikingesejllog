package com.example.vikingesejllog;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.IOException;

import static android.media.MediaRecorder.AudioEncoder.DEFAULT;



public class AudioRecorder {

    /*Author: s164497 - Hemsen

    Denne klasse har til formål at styre logikken bag lydoptagelsen inde i "Opret Note". Herved
    gemmes noten som en .MPEG4-fil. Filnavnet defineres ud fra hvilken note, der oprettes, således
    at hvert filnavn er unikt. Lige pt. gemmes de under music-mappen på SDkortet.
     */


    MediaRecorder audioRecorder;
    MediaPlayer audioPlayer;
    ProgressDialog progressDialog;



    public void recordAudio(String fileName) throws IOException {
        /*Denne metode gemmer en lydfil, der optages gennem mikrofonen. FileName skal vi have
        defineret som en variabel svarende til noten, der oprettes.
         */


        ContentValues values = new ContentValues(3);
        audioRecorder = new MediaRecorder();
        //fileName = et eller andet navn på noten
        values.put(MediaStore.MediaColumns.TITLE, fileName);
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRecorder.setAudioEncoder(DEFAULT);
        audioRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath()+"/vikingesejllog/lydnoter" + fileName);
        audioRecorder.prepare();

        audioRecorder.start();

        progressDialog.setTitle("Optager lydnote");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        progressDialog.setButton(1, "Afslut optagelse", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopAudioRecord();
            }
        });
    }

public void stopAudioRecord(){
        //Stopper optagelsen og frigiver objektet til garbage-collector
    progressDialog.dismiss();
    audioRecorder.stop();
    audioRecorder.release();
}


    public void playAudioNote(String fileName) throws IOException {
        //Husk fileName skal defineres enten her eller inden metoden kaldes.
        String filePath = Environment.getExternalStorageDirectory().getPath()+"/vikingesejllog/lydnoter" + fileName;

        audioPlayer = new MediaPlayer();
        audioPlayer.setDataSource(filePath);
        audioPlayer.setVolume(1, 1);
        audioPlayer.prepare();
        audioPlayer.start();

        progressDialog.setTitle("Afspiller lydnote");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        progressDialog.setButton(1, "Afslut afspilning", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopAudioNote();
            }
        });
    }

    public void stopAudioNote(){
        //Stopper afspilningen af noten.
        progressDialog.dismiss();
        audioPlayer.stop();
        audioPlayer.release();
    }


}
