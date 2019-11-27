package com.example.vikingesejllog;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.IOException;

import static android.media.MediaRecorder.AudioEncoder.DEFAULT;



public class AudioRecorder {

    MediaRecorder audioRecorder;
    ProgressDialog progressDialog;

    MediaPlayer audioPlayer;




    public void recordAudio(String fileName) throws IOException {
        //Mangler noget der gemmet noten med samme navn som selve noten..

        ContentValues values = new ContentValues(3);
        audioRecorder = new MediaRecorder();
        //fileName = et eller andet navn på noten
        values.put(MediaStore.MediaColumns.TITLE, fileName);
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRecorder.setAudioEncoder(DEFAULT);
        audioRecorder.setOutputFile("/sdcard/music" + fileName);
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
    progressDialog.dismiss();
    audioRecorder.stop();
    audioRecorder.release();
}


    public void playAudioNote(String fileName) throws IOException {
        //Skal også have noget, der afspiller den rigtige lydfil afhængig af navn på note.
        String filePath = Environment.getExternalStorageDirectory()+"/sdcard/music" + fileName;

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
        progressDialog.dismiss();
        audioPlayer.stop();
        audioPlayer.release();
    }


}
