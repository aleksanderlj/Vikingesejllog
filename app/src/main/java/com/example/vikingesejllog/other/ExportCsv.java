package com.example.vikingesejllog.other;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.model.Togt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;

public class ExportCsv {
    public static void export(Context context, Togt togt) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Appen skal kunne gemme filer for at exportere", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {

            AppDatabase db = DatabaseBuilder.get(context);

            Executors.newSingleThreadExecutor().execute(() -> {
                List<EtapeWithNotes> etaper = db.etapeDAO().getAllByTogtId(togt.getTogt_id());

                File dir = new File(Environment.getExternalStorageDirectory() + "/Sejllog/csv/");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                try {
                    File csv = new File(dir, togt.getName() + ".csv");
                    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(csv), StandardCharsets.UTF_8);
                    //FileWriter fw = new FileWriter(csv);
                    BufferedWriter bw = new BufferedWriter(osw);

                    // Togt info
                    String s = "TOGT:,Navn,Fra,Dato,,,,,";
                    bw.write(s);
                    bw.newLine();

                    s = String.format(",%s,%s,%s,,,,,", togt.getName(), togt.getDeparture(), togt.getDepartureDate());
                    bw.write(s);
                    bw.newLine();

                    s = ",,,,,,,,";

                    bw.write(s);
                    bw.newLine();
                    bw.write(s);
                    bw.newLine();



                    for (EtapeWithNotes e : etaper) {
                        // Etape info
                        s = "ETAPE:,Fra-til,Afrejse dato,Skipper,Besætning,,,,";
                        bw.write(s);
                        bw.newLine();

                        s = String.format(",%s,%s,%s,\"%s\",,,,",
                                e.etape.getStart() + " - " + e.etape.getEnd(), e.etape.getDeparture(), e.etape.getSkipper(), e.etape.getCrew().toString());
                        bw.write(s);
                        bw.newLine();

                        s = ",,,,,,,,";
                        bw.write(s);
                        bw.newLine();

                        s = ",Tid,\"GPS\",Vindhastighed,Sejlføring,Sejlstilling,Kurs,Roere,Kommentar";
                        bw.write(s);
                        bw.newLine();

                        // Note info
                        for (Note n : e.getNoteList()) {
                            s = String.format(",%s,\"%s\",%s,%s,%s,%s,%s,%s",
                                    n.getTime(), n.getGpsLoc(), n.getWindSpeed(), n.getSailForing(), n.getSailStilling(), n.getCourse(), n.getRowers(), n.getComment());
                            s = s.replace("\n", " ");
                            bw.write(s);
                            bw.newLine();
                        }

                        s = ",,,,,,,,";

                        bw.write(s);
                        bw.newLine();
                        bw.write(s);
                        bw.newLine();

                    }


                    bw.flush();
                    bw.close();
                    ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "Export klar...", Toast.LENGTH_SHORT).show());

                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.setType("application/csv");
                    email.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", csv));
                    email.putExtra(Intent.EXTRA_SUBJECT, "Export af " + togt.getName());
                    context.startActivity(Intent.createChooser(email, "Send csv til..."));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
