package com.example.vikingesejllog.note;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.example.vikingesejllog.etape.CrewList;
import com.example.vikingesejllog.etape.CrewListItem;
import com.example.vikingesejllog.etape.CrewListViewOnly;
import com.example.vikingesejllog.etape.EtapeTopFragment;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.togt.TogtList;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.other.DatabaseBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class NoteList extends AppCompatActivity implements View.OnClickListener {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etape_activity_list);

        db = DatabaseBuilder.get(this);
        ActivityCompat.requestPermissions(NoteList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        navigationView = findViewById(R.id.nav_view);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        findViewById(R.id.menu_button).setOnClickListener(this);
        findViewById(R.id.newHarborButton).setOnClickListener(this);
        findViewById(R.id.etape_crewinfo).setOnClickListener(this);

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
                EtapeTopFragment f = (EtapeTopFragment) getSupportFragmentManager().findFragmentById(R.id.topMenuFragment);

                f.setAll(etaper.get(pager.getCurrentItem()));
                String s = "" + (pager.getCurrentItem() + 1) + "/" + (etaper.size());
                ((TextView) findViewById(R.id.pagecount)).setText(s);

                runOnUiThread(() -> {
                    updateNavButtons();
                });
            }
        });
    }

    private void updateNavButtons(){
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
                    startActivityForResult(newEtapeIntent, ETAPE_CODE);
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                    return true;
                case R.id.togt_oversigt:
                    finish();
                    return true;
                case R.id.exporter_csv:
                    // TODO do dis
                    return true;
            }
            mDrawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    // if navigation drawer is open backbutton will close it
    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.END))
            mDrawerLayout.closeDrawer(Gravity.END);
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
            case R.id.newHarborButton:
                if(!etaper.isEmpty()) {
                    Intent createnote = new Intent(this, CreateNote.class);
                    createnote.putExtra("etape_id", etaper.get(pager.getCurrentItem()).etape.getEtape_id());
                    this.startActivityForResult(createnote, NOTE_CODE);
                } else {
                    Toast toast = Toast.makeText(this, "Opret først etape",Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.etape_crewinfo:
                if (!etaper.isEmpty()){
                    Intent intent = new Intent(this, CrewListViewOnly.class);
                    intent.putExtra("skipper", getSkipperName());
                    intent.putExtra("crew", convertCrewListToJson());
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(this, "Opret først etape",Toast.LENGTH_SHORT);
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

        public void notifyNoteDataSetChanged(int pos){
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
                } else {
                    updateEtapeList(savedPos);
                    ((NotePagerAdapter) adapter).notifyNoteDataSetChanged(pager.getCurrentItem());
                }
            });
        }
    }

    private void updateEtapeList(int startPos) {
        List<EtapeWithNotes> newEtaper = db.etapeDAO().getAllByTogtId(togt.getTogt_id());
        if(startPos == LASTETAPE){
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
            });
        } else {
            Intent i = new Intent(this, CreateEtape.class);
            i.putExtra("togt_id", togt.getTogt_id());
            startActivityForResult(i, ETAPE_CODE);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        savedPos = pager.getCurrentItem();
        super.startActivityForResult(intent, requestCode);
    }

    private Etape getEtape(){
        EtapeWithNotes etapeWithNotes = etaper.get(pager.getCurrentItem());
        return etapeWithNotes.getEtape();
    }

    private ArrayList<CrewListItem> convertCrewToListItems(){
        ArrayList<CrewListItem> crewListItems = new ArrayList<>();
        List<String> crewNames;
        crewNames = getEtape().getCrew();
        for (int i = 0; i < crewNames.size(); i++){
            CrewListItem crewListItem = new CrewListItem(crewNames.get(i));
            crewListItems.add(crewListItem);
        }
        return crewListItems;
    }

    private String convertCrewListToJson(){
        ArrayList<CrewListItem> crewListItems = convertCrewToListItems();
        Gson gson = new Gson();
        return gson.toJson(crewListItems);
    }

    private String getSkipperName(){
        Etape etape = getEtape();
        return etape.getSkipper();
    }
}
