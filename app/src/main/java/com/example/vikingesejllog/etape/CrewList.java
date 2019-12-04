package com.example.vikingesejllog.etape;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;

import java.util.ArrayList;
import java.util.List;

public class CrewList extends AppCompatActivity {

    private List<CrewListItem> crewListItems;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_crewlist);

        recyclerView = findViewById(R.id.crewRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        crewListItems = new ArrayList<>();

        // test for crew list '
        // tilføjer 10 crewmembers til en liste
        //TODO: lav logik så man kan fjerne og tiljøfe besætning
        for(int i=0; i < 10; i++){

            CrewListItem crewListItem = new CrewListItem("Crew " + (i+1));

            crewListItems.add(crewListItem);
        }

        adapter = new CrewListAdapter(crewListItems,this);
        recyclerView.setAdapter(adapter);
    }
}
