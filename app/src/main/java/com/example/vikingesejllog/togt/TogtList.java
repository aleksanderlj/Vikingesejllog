package com.example.vikingesejllog.togt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.TopMenu;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.note.NoteList;
import com.example.vikingesejllog.other.DatabaseBuilder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TogtList extends AppCompatActivity implements View.OnClickListener {
	private SharedPreferences prefs;
    private RecyclerView recyclerView;
    private TogtListAdapter adapter;
    private List<Togt> togtList;
    private AppDatabase db = DatabaseBuilder.get(this);
    
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
		AppDatabase db = DatabaseBuilder.get(this);
    }

    public void updateList(){
		togtList = db.togtDAO().getAll();
		adapter = new TogtListAdapter(togtList, this);
		recyclerView.setAdapter(adapter);
	
		adapter.setOnItemClickListener(new TogtListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position) {
				Intent noteList = new Intent(TogtList.this, NoteList.class);
				Togt currTogt = db.togtDAO().getById(togtList.get(position).getTogt_id());
				noteList.putExtra("togt_id", currTogt.getTogt_id());
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
