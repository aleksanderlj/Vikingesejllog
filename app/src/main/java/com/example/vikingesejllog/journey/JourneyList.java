package com.example.vikingesejllog.journey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.TopMenu;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Togt;

import java.util.ArrayList;
import java.util.List;

public class JourneyList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private JourneyListAdapter adapter;

    private List<Togt> togtList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_list);

        recyclerView = (RecyclerView) findViewById(R.id.journeyRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        togtList = new ArrayList<>();
        List<Etape> exampleEtapeList = new ArrayList<>();


        // Test list items
        for (int i = 0; i<=10; i++){
            Togt togt = new Togt("Nyborg -",
                    "Skagen");
            togtList.add(togt);
        }

        adapter = new JourneyListAdapter(togtList, this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new JourneyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Logik her til tryk af element i recyclerview. Husk position starter fra 0.

            }
        });

        TopMenu tm = (TopMenu) getSupportFragmentManager().findFragmentById(R.id.topMenuFragment);
        tm.updateTextView("Liste over togter");

        findViewById(R.id.newHarborButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newHarborButton:
                Intent i = new Intent(this, NewJourney.class);
                startActivity(i);
                break;
        }
    }
}
