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

import com.example.vikingesejllog.MainActivity;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.test.TestData;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NoteList extends AppCompatActivity {

    private ViewPager2 pager;
    private RecyclerView.Adapter adapter;
    private ArrayList<Etape> etaper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
        ActivityCompat.requestPermissions(NoteList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);

        pager = findViewById(R.id.note_viewpager);

        //TODO These are tests
        TestData.createTestData();
        etaper = TestData.togter.get(1).getEtapeList();

        adapter = new NotePagerAdapter(getSupportFragmentManager(), getLifecycle(), etaper);
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
        public NotePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<Etape> etaper) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if(position<etaper.size()) {
                NoteListFragment f = new NoteListFragment(etaper.get(position).getNoteList());
                return f;
            } else {
                NewEtapeFragment newEtape = new NewEtapeFragment();
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
            String json = data.getStringExtra("etape");
            Gson gson = new Gson();
            Etape newEtape = gson.fromJson(json, Etape.class);
            etaper.add(newEtape);
            pager.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK){
            String json = data.getStringExtra("note");
            Gson gson = new Gson();
            Note newNote = gson.fromJson(json, Note.class);
            etaper.get(pager.getCurrentItem()).getNoteList().add(newNote);
            pager.setAdapter(adapter);
        }
    }
}
