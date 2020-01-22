package com.example.vikingesejllog.note;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.etape.CreateEtape;
import com.example.vikingesejllog.etape.crew.CrewListItem;
import com.example.vikingesejllog.etape.crew.CrewListViewOnly;
import com.example.vikingesejllog.etape.EtapeTopFragment;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.other.AppInfoActivity;
import com.example.vikingesejllog.other.DatabaseBuilder;
import com.example.vikingesejllog.other.ExportCsv;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import im.delight.android.location.SimpleLocation;

public class NoteList extends AppCompatActivity implements View.OnClickListener, EtapeTopFragment.UpdateEtapeTopFrag {
    private ViewPager2 pager;
    private RecyclerView.Adapter adapter;
    private DrawerLayout mDrawerLayout;
    private ImageButton nextButton, prevButton;
    private ArrayList<EtapeWithNotes> etaper;
    private Togt togt;
    private AppDatabase db;
    private final int ETAPE_CODE = 1, NOTE_CODE = 2, LASTETAPE = -1;
    private int savedPos;
    private WormDotsIndicator dotNavigation;
    private NavigationView navigationView;
    private EtapeTopFragment topFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etape_activity_pager);

        db = DatabaseBuilder.get(this);
        ActivityCompat.requestPermissions(NoteList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        navigationView = findViewById(R.id.nav_view);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        findViewById(R.id.menu_button).setOnClickListener(this);
        findViewById(R.id.newNoteButton).setOnClickListener(this);
        findViewById(R.id.etape_crewinfo).setOnClickListener(this);

        topFrag = (EtapeTopFragment) getSupportFragmentManager().findFragmentById(R.id.topMenuFragment);
        topFrag.setUpdateEtapeTopFrag(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setOnClickListenerNavigationDrawer();

        etaper = new ArrayList<>();
        adapter = new NotePagerAdapter(getSupportFragmentManager(), getLifecycle());
        pager = findViewById(R.id.note_viewpager);
        pager.setAdapter(adapter);

        Intent i = getIntent();
        Executors.newSingleThreadExecutor().execute(() -> {
            togt = db.togtDAO().getById(i.getLongExtra("togt_id", -1L));
            updateEtapeList(LASTETAPE);
        });


        // Create navigation buttons.
        dotNavigation = findViewById(R.id.dotNavigator);
        dotNavigation.setViewPager2(pager);

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                topFrag.setAll(etaper.get(pager.getCurrentItem()), position);
                String s = "" + (pager.getCurrentItem() + 1) + "/" + (etaper.size());
                ((TextView) findViewById(R.id.pagecount)).setText(s);

                runOnUiThread(() -> {
                    updateNavButtons();
                });
            }
        });
    }

    private void updateNavButtons() {
        if (pager.getCurrentItem() == 0) {
            prevButton.setEnabled(false);
            prevButton.setVisibility(View.INVISIBLE);
        } else {
            prevButton.setEnabled(true);
            prevButton.setVisibility(View.VISIBLE);
        }
        if (pager.getAdapter().getItemCount() - 1 == pager.getCurrentItem()) {
            nextButton.setEnabled(false);
            nextButton.setVisibility(View.INVISIBLE);
        } else {
            nextButton.setEnabled(true);
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    public void setOnClickListenerNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.ny_etape:
                    Intent newEtapeIntent = new Intent(NoteList.this, CreateEtape.class);
                    newEtapeIntent.putExtra("togt_id", togt.getTogt_id());

                    // Hent sidste etape i togten, for autofyld af crew i CreateEtape.
                    Etape lastEtape = etaper.get(etaper.size() - 1).getEtape();
                    newEtapeIntent.putExtra("etape_id", lastEtape.getEtape_id());

                    startActivityForResult(newEtapeIntent, ETAPE_CODE);
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                    return true;
                case R.id.slet_etape:
                    AlertDialog.Builder ad = new AlertDialog.Builder(this);
                    ad.setTitle("Vil du slette denne etape?");
                    ad.setPositiveButton("Godkend", (dialog, which) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            db.etapeDAO().delete(etaper.get(pager.getCurrentItem()).etape);
                            etaper.remove(etaper.get(pager.getCurrentItem()));
                            updateEtapeList(pager.getCurrentItem());
                            mDrawerLayout.closeDrawer(GravityCompat.END);
                        });
                    });
                    ad.setNegativeButton("Fortryd", (dialog, which) -> {
                        dialog.cancel();
                    });

                    ad.create().show();
                    return true;
                case R.id.togt_oversigt:
                    finish();
                    return true;
                case R.id.exporter_csv:
                    ExportCsv.export(this, togt);
                    return true;
                case R.id.mob:
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Appen skal have GPS adgang for at logge MOB", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        Note mobNote = Note.GetEmptyNote();
                        SimpleLocation location;
                        MyGPS gps = new MyGPS(this);
                        location = gps.getLocation();
                        location.beginUpdates();
                        location = gps.getLocation();
                        location.endUpdates();

                        String gpsData = "LAT: " + String.format(Locale.US, "%.2f", location.getLatitude()) + "\n" +
                                "LON: " + String.format(Locale.US, "%.2f", location.getLongitude());

                        SimpleDateFormat clock = new SimpleDateFormat("HH.mm", Locale.getDefault());
                        String time = clock.format(new Date());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss", Locale.getDefault());
                        String fileName = sdf.format(new Date());

                        mobNote.setEtape_id(etaper.get(pager.getCurrentItem()).etape.getEtape_id());
                        mobNote.setComment("MOB");
                        mobNote.setHasComment(true);
                        mobNote.setTime(time);
                        mobNote.setGpsLoc(gpsData);
                        mobNote.setFileName(fileName);

                        Executors.newSingleThreadExecutor().execute(() -> {
                            db.noteDAO().insert(mobNote);
                            updateEtapeList(pager.getCurrentItem());
                        });
                        if (mDrawerLayout.isDrawerOpen(GravityCompat.END))
                            mDrawerLayout.closeDrawer(GravityCompat.END);
                    }
                    return true;
                case R.id.app_info:
                    Intent intent = new Intent(this, AppInfoActivity.class);
                    startActivity(intent);
            }
            mDrawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    // if navigation drawer is open backbutton will close it
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END))
            mDrawerLayout.closeDrawer(GravityCompat.END);
        else {
            super.onBackPressed();
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_button:
                mDrawerLayout.openDrawer(Gravity.END);
                break;
            case R.id.prevButton:
                pager.setCurrentItem(pager.getCurrentItem() - 1, true);
                break;
            case R.id.nextButton:
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                break;
            case R.id.newNoteButton:
                if (!etaper.isEmpty()) {
                    Intent createnote = new Intent(this, CreateNote.class);
                    createnote.putExtra("etape_id", etaper.get(pager.getCurrentItem()).etape.getEtape_id());
                    createnote.putExtra("crew_size", etaper.get(pager.getCurrentItem()).etape.getCrew().size());
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Appen skal have GPS adgang for at lave en note", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        this.startActivityForResult(createnote, NOTE_CODE);
                    }
                } else {
                    Toast toast = Toast.makeText(this, "Opret først etape", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.etape_crewinfo:
                if (!etaper.isEmpty()) {
                    Intent intent = new Intent(this, CrewListViewOnly.class);
                    intent.putExtra("skipper", getSkipperName());
                    intent.putExtra("crew", convertCrewListToJson());
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(this, "Opret først etape", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }

    private class NotePagerAdapter extends FragmentStateAdapter {
        List<NoteListFragment> fragments = new ArrayList<>();

        public NotePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            NoteListFragment f = new NoteListFragment(etaper.get(position));
            fragments.add(f);
            return f;
        }

        @Override
        public int getItemCount() {
            return etaper.size();
        }

        public void notifyNoteDataSetChanged(int pos) {
            runOnUiThread(() -> fragments.get(pos).getAdapter().notifyDataSetChanged());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ETAPE_CODE || requestCode == NOTE_CODE) && resultCode == Activity.RESULT_OK) {
            Executors.newSingleThreadExecutor().execute(() -> {
                if (requestCode == ETAPE_CODE) {
                    updateEtapeList(LASTETAPE);
                    runOnUiThread(() -> {

                        dotNavigation.addDot(etaper.size() - 1);
                    });
                } else {
                    updateEtapeList(savedPos);
                    ((NotePagerAdapter) adapter).notifyNoteDataSetChanged(pager.getCurrentItem());
                }
            });
        }
    }

    private void updateEtapeList(int startPos) {
        List<EtapeWithNotes> newEtaper = db.etapeDAO().getAllByTogtId(togt.getTogt_id());
        if (startPos == LASTETAPE) {
            startPos = newEtaper.size();
        }
        if (newEtaper.size() != 0) {
            etaper.clear();
            etaper.addAll(newEtaper);
            pager.post(() -> adapter.notifyDataSetChanged());
            int finalStartPos = startPos;
            runOnUiThread(() -> {
                pager.setAdapter(adapter);
                pager.setCurrentItem(finalStartPos, false); // Pageren klarer selv out of bounds exceptions

                String s = "" + (pager.getCurrentItem() + 1) + "/" + (etaper.size());
                ((TextView) findViewById(R.id.pagecount)).setText(s);
                updateNavButtons();
                topFrag.updateSpinner(etaper);
            });
        } else {
            Intent i = new Intent(this, CreateEtape.class);
            i.putExtra("togt_id", togt.getTogt_id());
            startActivityForResult(i, ETAPE_CODE);
            finish();
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        savedPos = pager.getCurrentItem();
        super.startActivityForResult(intent, requestCode);
    }

    private Etape getEtape() {
        EtapeWithNotes etapeWithNotes = etaper.get(pager.getCurrentItem());
        return etapeWithNotes.getEtape();
    }

    private ArrayList<CrewListItem> convertCrewToListItems() {
        ArrayList<CrewListItem> crewListItems = new ArrayList<>();
        List<String> crewNames;
        crewNames = getEtape().getCrew();
        for (int i = 0; i < crewNames.size(); i++) {
            CrewListItem crewListItem = new CrewListItem(crewNames.get(i));
            crewListItems.add(crewListItem);
        }
        return crewListItems;
    }

    private String convertCrewListToJson() {
        ArrayList<CrewListItem> crewListItems = convertCrewToListItems();
        Gson gson = new Gson();
        return gson.toJson(crewListItems);
    }

    private String getSkipperName() {
        Etape etape = getEtape();
        return etape.getSkipper();
    }

    @Override
    public void onSpinnerItemSelected(int position) {
        pager.setCurrentItem(position, true);
    }


}
