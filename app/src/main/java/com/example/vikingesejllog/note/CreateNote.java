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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.note.dialogs.NoteDialog;
import com.example.vikingesejllog.note.dialogs.NoteDialogListener;
import com.example.vikingesejllog.note.dialogs.NoteDialogSingleNumberPicker;
import com.example.vikingesejllog.note.dialogs.SailForingDialogFragment;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CreateNote extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, NoteDialogListener {

    // TODO Brug NumberPicker hvor du erstatter med Strings (du kan også kigge på Spinner)

    private MyGPS gps;
    private EditText windSpeed;
    private TextView windSpeedBtnText;
    private EditText course;
    private TextView courseBtnText;
    private TextView sailingSpeedBtnText;
    private EditText sailingSpeed;
    private EditText path;
    private TextView pathBtnText;
    private EditText direction;
    private TextView directionBtnText;
    private EditText rowers;
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

    private int STORAGE_PERMISSION_CODE = 1;
    private final int WIND_FIELD = 0, ROWERS_FIELD = 1, SAILFORING_FIELD = 2, SAILDIRECTION_FIELD = 3, COURSE_FIELD = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_createnote);

        db = DatabaseBuilder.get(this);
        windSpeed = findViewById(R.id.windspeedText);
        windSpeedBtnText = findViewById(R.id.windspeedButtonText);
        course = findViewById(R.id.courseText);
        courseBtnText = findViewById(R.id.courseButtonText);
        sailingSpeed = findViewById(R.id.sailingSpeed);
        sailingSpeedBtnText = findViewById(R.id.sailingSpeedButtonText);
        path = findViewById(R.id.pathText);
        pathBtnText = findViewById(R.id.pathButtonText);
        direction = findViewById(R.id.directionText);
        directionBtnText = findViewById(R.id.directionButtonText);
        rowers = findViewById(R.id.rowers);
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

        gps = new MyGPS(this);
        String s = "LAT: " + String.format(Locale.US, "%.2f", gps.getLocation().getLatitude()) + "\n" +
                "LON: " + String.format(Locale.US, "%.2f", gps.getLocation().getLongitude());
        System.out.println(s);
        gpsText.setText(s);

        MyTime time = new MyTime();
        timeText.setText(time.getTime());


    }

    public void setWindSpeed(final View v){

        windSpeedBtnText.setVisibility(View.INVISIBLE);

        windSpeed.setVisibility(View.VISIBLE);

        windSpeed.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        windSpeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    windSpeedBtnText.setText(windSpeed.getText() + " m/s");
                    windSpeedBtnText.setVisibility(View.VISIBLE);
                    v.setVisibility(View.VISIBLE);
                    windSpeed.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setCourse(final View v) {

        courseBtnText.setVisibility(View.INVISIBLE);

        course.setVisibility(View.VISIBLE);

        course.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        course.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    courseBtnText.setText(course.getText());
                    courseBtnText.setVisibility(View.VISIBLE);
                    v.setVisibility(View.VISIBLE);
                    course.setVisibility(View.INVISIBLE);

                }
            }
        });
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
        NoteDialog df = new NoteDialogSingleNumberPicker(s, SAILFORING_FIELD);
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "sailforing");


        /*NoteDialog df = new SailForingDialogFragment();
        df.setNoteDialogListener(this);
        df.show(getSupportFragmentManager().beginTransaction(), "sailforing");

         */


        /*
        pathBtnText.setVisibility(View.INVISIBLE);

        path.setVisibility(View.VISIBLE);

        path.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        path.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    pathBtnText.setText(path.getText());
                    pathBtnText.setVisibility(View.VISIBLE);
                    path.setVisibility(View.INVISIBLE);

                }
            }
        });
         */
    }

    public void setDirection(final View v) {

        directionBtnText.setVisibility(View.INVISIBLE);

        direction.setVisibility(View.VISIBLE);

        direction.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        direction.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    directionBtnText.setText(direction.getText());
                    directionBtnText.setVisibility(View.VISIBLE);
                    direction.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    public void setRowers(final View v) {

        //TODO Måske ændre PopupMenu til et PopupWindow som ser lidt pænere ud

        PopupMenu popup = new PopupMenu(this, v);

        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                rowersBtnText.setText(item.getTitle());
                rowersBtnText.setVisibility(View.VISIBLE);
                rowers.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        popup.show();
    }

    public void confirm(View v) {

        Note note = new Note(getIntent().getLongExtra("etape_id", -1L), gps.getLocation().getLatitude() + String.valueOf(gps.getLocation().getLongitude()).substring(0, 8),
                sailingSpeed.getText().toString(), windSpeed.getText().toString(), timeText.getText().toString(),
                rowers.getText().toString(), path.getText().toString(), direction.getText().toString(), course.getText().toString(), commentText.getText().toString());

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
    public void onSingleNumberPickerSelected(String value, int field){
        switch (field){
            case ROWERS_FIELD:
                rowersBtnText.setText(value);
                break;

            case SAILFORING_FIELD:
                pathBtnText.setText(value);
                break;

            case COURSE_FIELD:
                courseBtnText.setText(value);
                break;

        }
    }

    @Override
    public void onDoubleNumberPickerSelected(String value1, String value2, int field) {
        String s;
        switch (field){
            case WIND_FIELD:
                s = value1 + " " + value2 + " m/s";
                windSpeedBtnText.setText(s);
                break;

            case SAILDIRECTION_FIELD:
                s = value1 + " " + value2;
                directionBtnText.setText(s);
                break;
        }
    }

    /*
    @Override
    public void onWindSelected(String direction, String speed) {
        String s = direction + " " + speed + " m/s";
        windSpeedBtnText.setText(s);
    }

    @Override
    public void onRowersSelected(String rowers) {
        rowersBtnText.setText(rowers);
    }

    @Override
    public void onSailForingSelected(String sailForing) {
        pathBtnText.setText(sailForing);
    }

    @Override
    public void onSailDirectionSelected(String direction, String board) {
        String s = direction + " " + board;
        directionBtnText.setText(s);
    }

    @Override
    public void onCourseSelected(String course) {
        courseBtnText.setText(course);
    }

     */
}
