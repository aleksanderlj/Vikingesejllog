package com.example.vikingesejllog.note;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vikingesejllog.R;

import java.util.ArrayList;

public class NoteList extends AppCompatActivity {

    private ViewPager2 pager;
    private RecyclerView.Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);

        pager = findViewById(R.id.note_viewpager);
        adapter = new NotePagerAdapter(getSupportFragmentManager(), getLifecycle());
        pager.setAdapter(adapter);
    }

    private class NotePagerAdapter extends FragmentStateAdapter {
        private ArrayList<NoteListFragment> arrayList = new ArrayList<>();

        public NotePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new NoteListFragment();
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}
