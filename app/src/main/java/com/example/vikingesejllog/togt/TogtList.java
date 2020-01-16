package com.example.vikingesejllog.togt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.TopMenu;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.note.NoteList;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TogtList extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private TogtListAdapter adapter;
    private List<Togt> togtList;
    private AppDatabase db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.togt_activity_list);
        db = DatabaseBuilder.get(this);

        recyclerView = findViewById(R.id.journeyRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
		updateList();

        findViewById(R.id.newTogtButton).setOnClickListener(this);
        findViewById(R.id.delete_db).setOnClickListener(this);
    }

    public void updateList(){
        togtList = new ArrayList<>();
        adapter = new TogtListAdapter(togtList, this);
        recyclerView.setAdapter(adapter);

        Executors.newSingleThreadExecutor().execute(() -> {
            togtList.addAll(db.togtDAO().getAll());
            recyclerView.post(() -> adapter.notifyDataSetChanged());
        });

		adapter.setOnItemClickListener((int position) -> {
            Intent noteList = new Intent(TogtList.this, NoteList.class);
            noteList.putExtra("togt_id", togtList.get(position).getTogt_id());
            startActivity(noteList);

        });
	}
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newTogtButton:
                Intent i = new Intent(this, CreateTogt.class);
                startActivityForResult(i, 1);
                break;

            case R.id.delete_db:
                Executors.newSingleThreadExecutor().execute(() -> db.clearAllTables());
                break;
        }
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		updateList();
	}
}
