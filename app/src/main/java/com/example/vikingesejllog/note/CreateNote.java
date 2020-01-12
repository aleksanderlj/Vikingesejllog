package com.example.vikingesejllog.note;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.note.dialogs.NoteDialog;
import com.example.vikingesejllog.note.dialogs.NoteDialogNumberPicker;
import com.example.vikingesejllog.note.dialogs.NoteDialogListener;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CreateNote extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, NoteDialogListener {

    // TODO ryd op i alle findByViewID() declarations (vi behøver ikke finde dem alle fra start)

    private TextView windSpeedBtnText;
    private EditText course;
    private TextView courseBtnText;
    private TextView sailingSpeedBtnText;
    private EditText sailingSpeed;
    private TextView pathBtnText;
    private TextView directionBtnText;
    private TextView rowersBtnText;
    private TextView timeText;
    private TextView gpsText;
    private EditText commentText;

    private ImageButton micButton;
    private ImageButton cameraButton;
    private ImageView takenPicture, savedPicture;

    AudioRecorder audioRecorder;
    private boolean recordingDone;

    private AppDatabase db;

    private final int STORAGE_PERMISSION_CODE = 1;
    private final int WIND_FIELD = 0, ROWERS_FIELD = 1, SAILFORING_FIELD = 2, SAILDIRECTION_FIELD = 3, COURSE_FIELD = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_createnote);

        db = DatabaseBuilder.get(this);
        windSpeedBtnText = findViewById(R.id.windspeedButtonText);
        course = findViewById(R.id.courseText);
        courseBtnText = findViewById(R.id.courseButtonText);
        sailingSpeed = findViewById(R.id.sailingSpeed);
        sailingSpeedBtnText = findViewById(R.id.sailingSpeedButtonText);
        pathBtnText = findViewById(R.id.pathButtonText);
        directionBtnText = findViewById(R.id.directionButtonText);
        rowersBtnText = findViewById(R.id.rowersButtonText);
        timeText = findViewById(R.id.clockButtonText);
        gpsText = findViewById(R.id.coordsButtonText);
        commentText = findViewById(R.id.textComment);

        micButton = findViewById(R.id.micButton);
        micButton.setOnClickListener(this);
        audioRecorder = new AudioRecorder();

        cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(this);

        takenPicture = findViewById(R.id.takenPicture);
        takenPicture.setOnTouchListener(this);

        savedPicture = findViewById(R.id.savedPicture);
        savedPicture.setImageAlpha(0);

        MyGPS gps = new MyGPS(this);
        String s = "LAT: " + String.format(Locale.US, "%.2f", gps.getLocation().getLatitude()) + "\n" +
                "LON: " + String.format(Locale.US, "%.2f", gps.getLocation().getLongitude());
        gpsText.setText(s);

        MyTime time = new MyTime();
        timeText.setText(time.getTime());


    }

    public void setWindSpeed(final View v) {
        String[] s1 = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};

        int range = 33;
        String[] s2 = new String[range + 1];
        for (int n = 0; n < range; n++) {
            s2[n] = String.valueOf(n) + " m/s";
        }
        s2[s2.length - 1] = "33+";


        // TODO Direction skal gerne vælges fra et kompas, så skal nok lave en anden klasse
        NoteDialog df = new NoteDialogNumberPicker(WIND_FIELD, s1, s2);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "wind");
    }

    public void setCourse(final View v) {
        int range = 10;
        String[] s1 = {"0", "1", "2", "3"},
                s2 = new String[range],
                s3 = new String[range];

        for (int n = 0; n < range; n++) {
            s2[n] = String.valueOf(n);
            s3[n] = String.valueOf(n);
        }

        NoteDialog df = new NoteDialogNumberPicker(COURSE_FIELD, s1, s2, s3);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "wind");
    }

    public void setSailingSpeed(final View v) {

        sailingSpeedBtnText.setVisibility(View.INVISIBLE);

        sailingSpeed.setVisibility(View.VISIBLE);

        sailingSpeed.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        sailingSpeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    sailingSpeedBtnText.setText(sailingSpeed.getText() + " kn");
                    sailingSpeedBtnText.setVisibility(View.VISIBLE);
                    sailingSpeed.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setPath(final View v) {
        String[] s = {"F", "Ø", "N1", "N2", "N3"};

        NoteDialog df = new NoteDialogNumberPicker(SAILFORING_FIELD, s);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "sailforing");
    }

    public void setDirection(final View v) {
        String[] s1 = {"bi", "fo", "ha", "ag", "læ"};
        String[] s2 = {"sb", "bb"};

        NoteDialog df = new NoteDialogNumberPicker(SAILDIRECTION_FIELD, s1, s2);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "saildirection");
    }

    public void setRowers(final View v) {
        int range = 20; //TODO should be crewsize
        String[] s = new String[range + 1];
        for (int n = 0; n < range + 1; n++) {
            s[n] = String.valueOf(n);
        }

        NoteDialog df = new NoteDialogNumberPicker(ROWERS_FIELD, s);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "rowers");
    }

    public void confirm(View v) {

        Note note = new Note(getIntent().getLongExtra("etape_id", -1L), gpsText.getText().toString(),
                sailingSpeedBtnText.getText().toString(), windSpeedBtnText.getText().toString(), timeText.getText().toString(),
                rowersBtnText.getText().toString(), pathBtnText.getText().toString(), directionBtnText.getText().toString(), courseBtnText.getText().toString(), commentText.getText().toString());

        Executors.newSingleThreadExecutor().execute(() -> {
            db.noteDAO().insert(note);
            setResult(Activity.RESULT_OK);
            finish();
        });
    }

    @Override
    public void onClick(View v) {
        if (v == micButton && !recordingDone) {
            try {
                audioRecorder.recordAudio("test");
                micButton.setImageResource(R.drawable.nem);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (v == micButton && recordingDone) {
            //Skal køres først, for at sikre, at brugeren har givet tilladelse til appen.
            if (ContextCompat.checkSelfPermission(CreateNote.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                try {
                    audioRecorder.playAudioNote("test");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //Hvis tilladelsen ikke allerede er givet, skal der spørges efter den.
                requestStoragePermission();
            }
        }
        if (v == cameraButton) {
            //Sender intent til at åbne kameraet og afventer resultatet.
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Køres når der er et resultat fra kamera appen og gemmer det som et bitmap:
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        //Skal evt. gemmes i noget sharedprefs med navn på note..
        takenPicture.setImageBitmap(bitmap);
        savedPicture.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouch(View takenPicture, MotionEvent event) {
        /*Zoomer ind på billedet, hvis brugeren rør ved det, og zoomer ud igen,
        hvis fingeren slippes
         */
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            savedPicture.setImageAlpha(255);
            savedPicture.setElevation(100);
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            savedPicture.setImageAlpha(0);
            return true;
        }
        return false;
    }


    //Følgede to metoder beder brugeren om tilladelse til at tilgå enhedens lagerplads:
    public void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Tilladelse påkrævet!")
                    .setMessage("Følgende tilladelsen kræves for at kunne afspille gemte lydnoter")
                    .setPositiveButton("Godkend", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            {
                                ActivityCompat.requestPermissions(CreateNote.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                            }
                        }
                    })
                    .setNegativeButton("Afvis", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Tilladelsen blev givet", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Tilladelsen blev afvist", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNumberPickerSelected(String[] values, int field) {
        String s;

        switch (field) {
            case ROWERS_FIELD:
                rowersBtnText.setText(values[0]);
                break;

            case SAILFORING_FIELD:
                pathBtnText.setText(values[0]);
                break;

            case WIND_FIELD:
                s = values[0] + " " + values[1];
                windSpeedBtnText.setText(s);
                break;

            case SAILDIRECTION_FIELD:
                s = values[0] + " " + values[1];
                directionBtnText.setText(s);
                break;

            case COURSE_FIELD:
                s = values[0] + values[1] + values[2] + "\u00B0";
                courseBtnText.setText(s);
                break;
        }

    }
}
