package com.example.vikingesejllog.note;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

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
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vikingesejllog.etape.EtapeTopFragment;
import com.example.vikingesejllog.etape.CreateButton;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.etape.EtapeTopFragment;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.test.TestData;
import com.example.vikingesejllog.togt.TogtListAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NoteList extends AppCompatActivity implements View.OnClickListener {
    private ViewPager2 pager;
    private RecyclerView.Adapter adapter;
    private ArrayList<Etape> etaper;
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;
    private TogtListAdapter togtAdapter;
    private ArrayList<Togt> togt_list;
    private Button nextButton, prevButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etape_activity_list);

        Button button = findViewById(R.id.menu_button);
        button.setOnClickListener(this);

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
        //int currFragment;
        switch (v.getId()){
			case R.id.menu_button:
				mDrawerLayout.openDrawer(Gravity.END);
				break;
            /*case R.id.prevButton:
                System.out.println("Penis1");
                currFragment = pager.getCurrentItem();
                ((NotePagerAdapter)adapter).createFragment(currItem+1);
                pager.setCurrentItem(currItem-1, true);
                break;
            case R.id.nextButton:
                System.out.println("Penis2");
                currFragment = pager.getCurrentItem();
                ((NotePagerAdapter)adapter).createFragment(currItem+1);
                pager.setCurrentItem(currItem+1, true);
                break;*/
        }
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
                CreateButton newEtape = new CreateButton();
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
