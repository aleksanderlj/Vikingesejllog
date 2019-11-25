package com.example.vikingesejllog.journey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.TopMenu;

import java.util.ArrayList;
import java.util.List;

public class JourneyList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<JourneyListItem> journeyListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_list);

        recyclerView = (RecyclerView) findViewById(R.id.journeyRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        journeyListItems = new ArrayList<>();

        // Test list items
        for (int i = 0; i<=10; i++){
            JourneyListItem journeyListItem = new JourneyListItem("Etape " + (i+1),
                    "19/11-2019 - 21/11-2019");
            journeyListItems.add(journeyListItem);
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
