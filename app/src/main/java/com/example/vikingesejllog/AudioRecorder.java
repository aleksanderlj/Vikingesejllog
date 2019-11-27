package com.example.vikingesejllog;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MicrophoneDirection;
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
        //fileName = et eller andet navn på noten
        values.put(MediaStore.MediaColumns.TITLE, fileName);
        audioRecorder.setAudioSource(MicrophoneDirection.MIC_DIRECTION_TOWARDS_USER);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
        audioRecorder.setAudioEncoder(DEFAULT);
        audioRecorder.setOutputFile("/sdcard/sound/vikingesejllog" + fileName);
        audioRecorder.prepare();

        progressDialog.setTitle("Optager lydnote");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setButton("Afslut optagelse", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
                audioRecorder.stop();
                audioRecorder.release();
            }
        });
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                audioRecorder.stop();
                audioRecorder.release();
            }
        });
        audioRecorder.start();
        progressDialog.show();
    }

/*public void saveAudio(){
    audioRecorder.stop();
    audioRecorder.release();
}*/


    public void playAudio() throws IOException {
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


}
