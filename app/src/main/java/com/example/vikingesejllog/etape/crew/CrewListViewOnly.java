package com.example.vikingesejllog.etape.crew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.etape.crew.CrewListAdapter;
import com.example.vikingesejllog.etape.crew.CrewListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CrewListViewOnly extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CrewListAdapter listAdapter;
    private ArrayList<CrewListItem> crewListItems;
    private TextView skipperName;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_crewlist_view_only);
        intent = getIntent();

        crewListItems = new ArrayList<>();

        skipperName = findViewById(R.id.skipperName);
        recyclerView = findViewById(R.id.crewRecyclerView_viewOnly);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getCrewFromJson();
        skipperName.setText("Skipper: " + intent.getStringExtra("skipper"));

        listAdapter = new CrewListAdapter(crewListItems, this);
        recyclerView.setAdapter(listAdapter);
    }

    private void getCrewFromJson() {
        String crewJson = intent.getStringExtra("crew");

        if (crewJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CrewListItem>>() {
            }.getType();
            crewListItems = gson.fromJson(crewJson, type);
        }
    }
}
