package com.example.vikingesejllog;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MicrophoneDirection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.media.MediaRecorder.AudioEncoder.AAC;
import static android.media.MediaRecorder.AudioEncoder.DEFAULT;

public class NoteRecorder {

    /*Author: s164497 - Hemsen

    Denne klasse har til formål at styre logikken bag knaptrykkene på hhv. "Optag" og "Kamera",
    når brugeren er ved at lave en note. Herefter skal billedet eller lydfilen gemmes sammen med
    noten.

    Følgende skal tilføjes manifestet:
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />


    <uses-feature android:name="android.hardware.camera"
                  android:required="true" />

                  <provider
        android:name="android.support.v4.content.FileProvider"
        android:authorities="com.example.android.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths"></meta-data>
    </provider>



    */

MediaPlayer videoPlayer;
Uri test;
String currentPhotoPath;




static final int REQUEST_IMAGE_CAPTURE = 1;

SharedPreferences sharedPreferences;

    private File saveImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

public void takePicture(){
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageNote = null;
        try {
            imageNote = saveImageFile();
        } catch (IOException ex) {
            System.out.println("Failed to save image");
        }

        // Continue only if the File was successfully created
        if (imageNote != null) {
            Uri imageUri = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    imageNote);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }




public void showImageNote(){
//Et eller andet med at klikke på det nye imageview og det åbner op.
}
}
