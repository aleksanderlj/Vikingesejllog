package com.example.vikingesejllog.togt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.TopMenu;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.note.NoteList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TogtList extends AppCompatActivity implements View.OnClickListener {
	private SharedPreferences prefs;
    private RecyclerView recyclerView;
    private TogtListAdapter adapter;

    private List<Togt> togtList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.togt_activity_list);

        recyclerView = (RecyclerView) findViewById(R.id.journeyRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
		updateList();
        
        TopMenu tm = (TopMenu) getSupportFragmentManager().findFragmentById(R.id.topMenuFragment);
        tm.updateTextView("Liste over togter");

        findViewById(R.id.newTogtButton).setOnClickListener(this);
    }

    public void updateList(){
		togtList = new ArrayList<>();
		List<Etape> exampleEtapeList = new ArrayList<>();
	
	
		// Test list items
		Togt togt;
		Gson gson = new Gson();
		prefs = getSharedPreferences("togtList", MODE_PRIVATE);
		Map<String,?> togtHash = prefs.getAll();
		for (int i = 1; i<=togtHash.size(); i++){
			String currTogt = (String) togtHash.get(Integer.toString(i));
			if (currTogt != null) {
				togt = gson.fromJson(currTogt, Togt.class);
				togt = new Togt(togt.getDeparture(), togt.getDestination());
				togtList.add(togt);
			}
			else
				break;
		}
		adapter = new TogtListAdapter(togtList, this);
		recyclerView.setAdapter(adapter);
	
		adapter.setOnItemClickListener(new TogtListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position) {
				Intent noteList = new Intent(TogtList.this, NoteList.class);
				Gson gson = new Gson();
				
				ArrayList<Etape> etapeList = togtList.get(position).getEtapeList();
				String jsonEtapeList = gson.toJson(etapeList);
				noteList.putExtra("etaper", jsonEtapeList);
				startActivity(noteList);
			}
		});
	}
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newTogtButton:
                Intent i = new Intent(this, CreateTogt.class);
                startActivityForResult(i, 1);
                break;
        }
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		updateList();
	}
}
