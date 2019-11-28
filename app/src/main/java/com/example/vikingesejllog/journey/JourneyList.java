package com.example.vikingesejllog.journey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.TopMenu;
import com.example.vikingesejllog.model.Togt;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JourneyList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<JourneyListItem> journeyListItems;
    private SharedPreferences prefs;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_list);

        recyclerView = (RecyclerView) findViewById(R.id.journeyRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        journeyListItems = new ArrayList<>();

        Gson gson = new Gson();
        prefs = getSharedPreferences("togtListe", MODE_PRIVATE);
        Map<String,?> togtHash = prefs.getAll();
        // Test list items
        for (Integer i = 0; i<=togtHash.size(); i++){
            String currTogt = (String) togtHash.get(i.toString());
            if (currTogt != null) {
                Togt togt = gson.fromJson(currTogt, Togt.class);
                JourneyListItem journeyListItem = new JourneyListItem(togt.getStart() + " - " + togt.getEnd(),
                        "");
                journeyListItems.add(journeyListItem);
            }
            else
                break;
        }
        
        adapter = new JourneyListAdapter(journeyListItems, this);
        recyclerView.setAdapter(adapter);

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
