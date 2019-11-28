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
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.test.TestData;

import java.util.ArrayList;

public class NoteList extends AppCompatActivity {

    private ViewPager2 pager;
    private RecyclerView.Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);

        pager = findViewById(R.id.note_viewpager);

        //TODO These are tests
        TestData.createTestData();
        final ArrayList<Etape> testList = TestData.togter.get(1).getEtapeList();

        adapter = new NotePagerAdapter(getSupportFragmentManager(), getLifecycle(), testList);
        pager.setAdapter(adapter);

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                EtapeTopFragment f = (EtapeTopFragment) getSupportFragmentManager().findFragmentById(R.id.topMenuFragment);
                f.setAll(testList.get(pager.getCurrentItem()), pager.getCurrentItem(), testList.size());
                System.out.println(pager.getCurrentItem());
            }
        });
    }

    private class NotePagerAdapter extends FragmentStateAdapter {
        private ArrayList<NoteListFragment> etapeFragments = new ArrayList<>();
        private ArrayList<Etape> etaper;

        public NotePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<Etape> etapes) {
            super(fragmentManager, lifecycle);
            this.etaper = etapes;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if(position<etaper.size()) {
                NoteListFragment f = new NoteListFragment();
                return f;
            } else {
                System.out.println("");
            }

        }

        @Override
        public int getItemCount() {
            return etaper.size()+1;
        }

        public void setEtapeFragments(ArrayList<NoteListFragment> etapeFragments) {
            this.etapeFragments = etapeFragments;
        }
    }
}
