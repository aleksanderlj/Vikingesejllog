package com.example.vikingesejllog.note;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.etape.EtapeTopFragment;
import com.example.vikingesejllog.etape.CreateButton;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.togt.TogtListAdapter;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.other.DatabaseBuilder;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class NoteList extends AppCompatActivity implements View.OnClickListener {
    private ViewPager2 pager;
    private RecyclerView.Adapter adapter;
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;
    private TogtListAdapter togtAdapter;
    private ArrayList<Togt> togt_list;
    private Button nextButton, prevButton;
    private ArrayList<EtapeWithNotes> etaper;
    private Togt togt;
    private AppDatabase db;
    private final int ETAPE_CODE = 1;
    private final int NOTE_CODE = 2;
    private int savedPos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etape_activity_list);

        Button button = findViewById(R.id.menu_button);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        button.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        
        togt_list = new ArrayList<>();

        mDrawerLayout = findViewById(R.id.drawer_layout);

        recyclerView = findViewById(R.id.togt_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TODO Indlæs alle togter ind i togt_list
        //Måske skal det her rykkes ind i onClick metoden i stedet for onCreate?

        togtAdapter = new TogtListAdapter(togt_list,this);
        recyclerView.setAdapter(togtAdapter);



        ActivityCompat.requestPermissions(NoteList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        db = DatabaseBuilder.get(this);
        etaper = new ArrayList<>();
        ActivityCompat.requestPermissions(NoteList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        pager = findViewById(R.id.note_viewpager);
        adapter = new NotePagerAdapter(getSupportFragmentManager(), getLifecycle());

        Intent i = getIntent();
        Executors.newSingleThreadExecutor().execute(() -> {
            togt = db.togtDAO().getById(i.getLongExtra("togt_id", -1L));
            List<EtapeWithNotes> newEtaper = db.etapeDAO().getAllByTogtId(togt.getTogt_id());
            etaper.clear();
            etaper.addAll(newEtaper);
            pager.post(() -> adapter.notifyDataSetChanged());
            pager.setCurrentItem(etaper.size()-1, false); // setCurrentItem klarer selv OutOfBounds execptions O.O

            if(pager.getCurrentItem()<etaper.size()) {
                String s = "" + (pager.getCurrentItem() + 1) + "/" + (etaper.size());
                ((TextView) findViewById(R.id.pagecount)).setText(s);
            }
        });

        pager.setAdapter(adapter);
        WormDotsIndicator dotNavigation = findViewById(R.id.dotNavigator);
        dotNavigation.setViewPager2(pager);
        
        
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                EtapeTopFragment f = (EtapeTopFragment) getSupportFragmentManager().findFragmentById(R.id.topMenuFragment);
                if (pager.getCurrentItem() < etaper.size()) {
                    getSupportFragmentManager().beginTransaction().show(f).commit();
                    f.setAll(etaper.get(pager.getCurrentItem()), pager.getCurrentItem(), etaper.size());
                    dotNavigation.setViewPager2(pager);
                    String s = "" + (pager.getCurrentItem()+1) + "/" + (etaper.size());
                    ((TextView)findViewById(R.id.pagecount)).setText(s);
                } else {
                    getSupportFragmentManager().beginTransaction().hide(f).commit();
                    // TODO top fragment needs to change when it reaches the end of viewpager
                }
                if (pager.getCurrentItem() == 0)
                    prevButton.setEnabled(false);
                else
                    prevButton.setEnabled(true);
                if (pager.getAdapter().getItemCount()-1 == pager.getCurrentItem()) {
                    nextButton.setEnabled(false);
                    ((TextView)findViewById(R.id.pagecount)).setText("");
                }
                else
                    nextButton.setEnabled(true);
            }
        });
    }

    // if navigation drawer is open backbutton will close it
    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.END))
            mDrawerLayout.closeDrawer(Gravity.END);
        else{
            super.onBackPressed();
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
			case R.id.menu_button:
				mDrawerLayout.openDrawer(Gravity.END);
				break;
            case R.id.prevButton:
                pager.setCurrentItem(pager.getCurrentItem() - 1, true);
                break;
            case R.id.nextButton:
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
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
            if (position < etaper.size()) {
                NoteListFragment f = new NoteListFragment(etaper.get(position));
                return f;
            } else {
                CreateButton createEtape = new CreateButton(togt.getTogt_id());
                return createEtape;
            }
        }

        @Override
        public int getItemCount() {
            return etaper.size() + 1;
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
                    if(requestCode == ETAPE_CODE) {
                        pager.setCurrentItem(etaper.size() - 1, false);
                    } else {
                        pager.setCurrentItem(savedPos, false);
                    }
                });
                //pager.post(() -> adapter.notifyDataSetChanged());
            });
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        savedPos = pager.getCurrentItem();
        super.startActivityForResult(intent, requestCode);
    }
}
