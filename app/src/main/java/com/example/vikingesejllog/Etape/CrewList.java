package com.example.vikingesejllog.etape;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;

import java.util.ArrayList;

public class CrewList extends AppCompatActivity implements View.OnClickListener, CrewListener {

    private Button newCrewButton;
    // Fragment

    private EditText edittextCrewName;
    private Button buttonConfirm;

    private RecyclerView recyclerView;
    private CrewListAdapter listAdapter;
    ArrayList<CrewListItem> crewListItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_crewlist);

        crewListItems = new ArrayList<>();

        recyclerView = findViewById(R.id.crewRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // test at crewListItems går ind i recyclerView
        /*
        for (int i=0; i<10; i++){
            CrewListItem crewListItem = new CrewListItem("crew "+(i+1));
            crewListItems.add(crewListItem);
        }
        */
        listAdapter = new CrewListAdapter(crewListItems, this);
        recyclerView.setAdapter(listAdapter);

        newCrewButton = findViewById(R.id.newCrewButton);
        newCrewButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == newCrewButton) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.crewNameFragment, new AddCrewFragment())
                    .addToBackStack(null)
                    .commit();

        }
    }

    @Override
    public void onMemberSelected(String member) {
        CrewListItem crewListItem = new CrewListItem(member);
        crewListItems.add(crewListItem);
        listAdapter.notifyItemInserted(crewListItems.size() - 1);
    }
}
