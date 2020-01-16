package com.example.vikingesejllog.note;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.etape.CreateEtape;
import com.example.vikingesejllog.etape.EtapeTopFragment;
import com.example.vikingesejllog.etape.CreateButton;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.togt.CreateTogt;
import com.example.vikingesejllog.togt.TogtList;
import com.example.vikingesejllog.togt.TogtListAdapter;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.other.DatabaseBuilder;
import com.google.android.material.navigation.NavigationView;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class NoteList extends AppCompatActivity implements View.OnClickListener {
    private ViewPager2 pager;
    private RecyclerView.Adapter adapter;
    private DrawerLayout mDrawerLayout;
    private Button newTogt;
    private ImageButton nextButton, prevButton;
    private ArrayList<EtapeWithNotes> etaper;
    private Togt togt;
    private AppDatabase db;
    private final int ETAPE_CODE = 1;
    private final int NOTE_CODE = 2;
    private final int FIRST_TOGT = 3;
    private int savedPos;
    private WormDotsIndicator dotNavigation;
    private NavigationView navigationView;
    private Context c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etape_activity_list);

        db = DatabaseBuilder.get(this);
        c = this;
        ImageView button = findViewById(R.id.menu_button);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        button.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationView = findViewById(R.id.nav_view);

        ActivityCompat.requestPermissions(NoteList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        etaper = new ArrayList<>();
        ActivityCompat.requestPermissions(NoteList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        pager = findViewById(R.id.note_viewpager);
        adapter = new NotePagerAdapter(getSupportFragmentManager(), getLifecycle());

        Intent i = getIntent();
        Executors.newSingleThreadExecutor().execute(() -> {
            if (i.getLongExtra("togt_id", -1L) != -1L) {
                togt = db.togtDAO().getById(i.getLongExtra("togt_id", -1L));
            } else {
                togt = db.togtDAO().getLatestTogt();
            }
            if (togt != null) {
                List<EtapeWithNotes> newEtaper = db.etapeDAO().getAllByTogtId(togt.getTogt_id());
                etaper.clear();
                etaper.addAll(newEtaper);
                pager.post(() -> adapter.notifyDataSetChanged());
                pager.setCurrentItem(etaper.size() - 1, false); // setCurrentItem klarer selv OutOfBounds execptions O.O
            } else {
                togt = new Togt();
                togt.setTogt_id(1L);
                Intent newIntent = new Intent(this, CreateTogt.class);
                startActivityForResult(newIntent, FIRST_TOGT);
            }
            pager.post(() -> adapter.notifyDataSetChanged());
            runOnUiThread(() -> {
                pager.setCurrentItem(etaper.size() - 1, false); // setCurrentItem klarer selv OutOfBounds execptions O.O

                if (pager.getCurrentItem() < etaper.size()) {
                    String s = "" + (pager.getCurrentItem() + 1) + "/" + (etaper.size());
                    ((TextView) findViewById(R.id.pagecount)).setText(s);
                }
            });
        });

        setOnClickListenerNavigationDrawer();

        pager.setAdapter(adapter);

        // Create navigation buttons.
        dotNavigation = findViewById(R.id.dotNavigator);
        dotNavigation.setViewPager2(pager);

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                EtapeTopFragment f = (EtapeTopFragment) getSupportFragmentManager().findFragmentById(R.id.topMenuFragment);

                f.setAll(etaper.get(pager.getCurrentItem()), pager.getCurrentItem(), etaper.size());
                dotNavigation.setViewPager2(pager);
                String s = "" + (pager.getCurrentItem() + 1) + "/" + (etaper.size());
                ((TextView) findViewById(R.id.pagecount)).setText(s);


                runOnUiThread(() -> {
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
                });
            }
        });
    }

    public void setOnClickListenerNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ny_etape:
                        Intent newEtapeIntent = new Intent(c, CreateEtape.class);
                        newEtapeIntent.putExtra("togt_id", togt.getTogt_id());
                        startActivityForResult(newEtapeIntent, 1);
                        mDrawerLayout.closeDrawer(GravityCompat.END);
                        return true;
                    case R.id.togt_oversigt:
                        Intent newTogtOversigtIntent = new Intent(c, TogtList.class);
                        startActivity(newTogtOversigtIntent);
                        return true;
                    case R.id.exporter_csv:
                        return true;
                }
                mDrawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }

        });

        findViewById(R.id.newHarborButton).setOnClickListener(this);
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
                Intent createnote = new Intent(this, CreateNote.class);
                createnote.putExtra("etape_id", etaper.get(pager.getCurrentItem()).etape.getEtape_id());
                this.startActivityForResult(createnote, NOTE_CODE);
                break;
        }
    }

    private class NotePagerAdapter extends FragmentStateAdapter {
        public NotePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            NoteListFragment f = new NoteListFragment(etaper.get(position));
            return f;
        }

        @Override
        public int getItemCount() {
            return etaper.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ETAPE_CODE || requestCode == NOTE_CODE) && resultCode == Activity.RESULT_OK) {
            Executors.newSingleThreadExecutor().execute(() -> {
                List<EtapeWithNotes> newEtaper = db.etapeDAO().getAllByTogtId(togt.getTogt_id());
                etaper.clear();
                etaper.addAll(newEtaper);
                pager.post(() -> {
                    pager.setAdapter(adapter);
                    if (requestCode == ETAPE_CODE) {
                        pager.setCurrentItem(etaper.size() - 1, false);
                    } else {
                        pager.setCurrentItem(savedPos, false);
                    }
                });
                //pager.post(() -> adapter.notifyDataSetChanged());
            });
        } else if (requestCode == FIRST_TOGT && resultCode == Activity.RESULT_OK) {
            Executors.newSingleThreadExecutor().execute(() -> {
                togt = db.togtDAO().getLatestTogt();
                List<EtapeWithNotes> newEtaper = db.etapeDAO().getAllByTogtId(togt.getTogt_id());
                etaper.clear();
                etaper.addAll(newEtaper);
                pager.post(() -> adapter.notifyDataSetChanged());
                runOnUiThread(() -> {
                    pager.setAdapter(adapter);
                    pager.setCurrentItem(etaper.size() - 1, false);
                });
            });
        } else if (requestCode == FIRST_TOGT && resultCode == Activity.RESULT_CANCELED) {
            Intent newIntent = new Intent(this, CreateTogt.class);
            startActivityForResult(newIntent, FIRST_TOGT);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        savedPos = pager.getCurrentItem();
        super.startActivityForResult(intent, requestCode);
    }
}
