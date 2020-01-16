package com.example.vikingesejllog.etape;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CrewList extends AppCompatActivity implements View.OnClickListener, CrewListener {

    private Button acceptCrewButton;
    private FloatingActionButton newCrewButton;

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

        Intent i = getIntent();
        String crewJson = i.getStringExtra("crew");

        if (crewJson != null ){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CrewListItem>>() {
            }.getType();
            crewListItems = gson.fromJson(crewJson, type);
        }

        listAdapter = new CrewListAdapter(crewListItems, this);
        recyclerView.setAdapter(listAdapter);

        acceptCrewButton = findViewById(R.id.acceptCrewButton);
        acceptCrewButton.setOnClickListener(this);

        newCrewButton = findViewById(R.id.newCrewButton);
        newCrewButton.setOnClickListener(this);




        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                removeCrewItem(viewHolder.getAdapterPosition());
                listAdapter.notifyDataSetChanged();
            }
        };

        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onClick(View v) {

        if (v == newCrewButton) {
            disableButtons();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.crewNameFragment, new AddCrewFragment())
                    .addToBackStack(null)
                    .commit();

        } else if (v == acceptCrewButton){
            Gson gson = new Gson();
            String json = gson.toJson(crewListItems);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("crewList", json);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    @Override
    public void onMemberSelected(String member) {
        CrewListItem crewListItem = new CrewListItem(member);
        crewListItems.add(crewListItem);
        listAdapter.notifyItemInserted(crewListItems.size() - 1);
    }

    @Override
    public void enableButtons() {
        newCrewButton.setPressed(false);
        newCrewButton.setClickable(true);
        acceptCrewButton.setPressed(false);
        acceptCrewButton.setClickable(true);
    }

    private void disableButtons(){
        newCrewButton.setPressed(true);
        newCrewButton.setClickable(false);
        acceptCrewButton.setPressed(true);
        acceptCrewButton.setClickable(false);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof AddCrewFragment) {
            AddCrewFragment addCrewFragment = (AddCrewFragment) fragment;
            ((AddCrewFragment) fragment).setCrewListener(this);

        }
    }

    public void removeCrewItem(int postion){
                crewListItems.remove(postion);
    }
}