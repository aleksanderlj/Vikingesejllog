package com.example.vikingesejllog.note;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.etape.EtapeTopFragment;
import com.example.vikingesejllog.etape.CreateButton;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.other.DatabaseBuilder;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class NoteList extends AppCompatActivity {

    private ViewPager2 pager;
    private RecyclerView.Adapter adapter;
    private ArrayList<EtapeWithNotes> etaper;
    private Togt togt;
    private AppDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseBuilder.get(this);
        setContentView(R.layout.etape_activity_list);
        ActivityCompat.requestPermissions(NoteList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);

        pager = findViewById(R.id.note_viewpager);

        Intent i = getIntent();
        togt = db.togtDAO().getById(i.getLongExtra("togt_id", -1L));
        etaper = (ArrayList<EtapeWithNotes>) db.etapeDAO().getAllByTogtId(togt.getTogt_id());

        adapter = new NotePagerAdapter(getSupportFragmentManager(), getLifecycle());
        pager.setAdapter(adapter);

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                EtapeTopFragment f = (EtapeTopFragment) getSupportFragmentManager().findFragmentById(R.id.topMenuFragment);
                if (pager.getCurrentItem() < etaper.size()) {
                    f.setAll(etaper.get(pager.getCurrentItem()), pager.getCurrentItem(), etaper.size());
                } else {
                    // TODO top fragment needs to change when it reaches the end of viewpager
                }
            }
        });
    }

    private class NotePagerAdapter extends FragmentStateAdapter {
        public NotePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if(position<etaper.size()) {
                NoteListFragment f = new NoteListFragment(etaper.get(position));
                return f;
            } else {
                CreateButton newEtape = new CreateButton(togt.getTogt_id());
                return newEtape;
            }
        }

        @Override
        public int getItemCount() {
            return etaper.size()+1;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            etaper = (ArrayList<EtapeWithNotes>) db.etapeDAO().getAllByTogtId(togt.getTogt_id());
            pager.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK){
            /*
            String json = data.getStringExtra("note");
            Gson gson = new Gson();
            Note newNote = gson.fromJson(json, Note.class);
            etaper.get(pager.getCurrentItem()).getNoteList().add(newNote);
             */
            pager.setAdapter(adapter);
        }
    }
}
