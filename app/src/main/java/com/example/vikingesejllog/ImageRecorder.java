package com.example.vikingesejllog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageRecorder extends AppCompatActivity {

    /*Author: s164497 - Hemsen

    Denne klasse har til formål at styre logikken bag kamera-funktionen inde i "Opret Note". Herved
    gemmes billedet som et imageview. Filnavnet defineres ud fra hvilken note, der oprettes, således
    at hvert filnavn er unikt.
    */

    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 0);
    }


   public void savePicture(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
    }

}












   /* static final int REQUEST_IMAGE_CAPTURE = 1;

    SharedPreferences sharedPreferences;

    private File saveImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
              ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /*public void takePicture(){
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
    }*/
