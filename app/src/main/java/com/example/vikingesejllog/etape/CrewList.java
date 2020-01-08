package com.example.vikingesejllog.etape;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;

import java.util.ArrayList;
import java.util.List;

public class CrewList extends AppCompatActivity {

    public List<CrewListItem> getCrewListItems() {
        return crewListItems;
    }

    private List<CrewListItem> crewListItems;

    private RecyclerView mRecyclerView;
    private CrewListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button newCrewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_crewlist);

        newCrewButton = findViewById(R.id.newCrewButton);
        newCrewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        createCrewList();
        buildRecyclerView();


    }

    public void newCrewMember(int position){

    }

    public void createCrewList(){
        crewListItems = new ArrayList<>();
        // test for crew list
        // tilføjer 10 crewmembers til en liste
        for(int i=0; i < 22; i++){
            CrewListItem crewListItem = new CrewListItem("Crew " + (i+1));
            crewListItems.add(crewListItem);
        }

    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.crewRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter = new CrewListAdapter(crewListItems,this);
    }

}
