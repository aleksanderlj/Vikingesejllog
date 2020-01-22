package com.example.vikingesejllog.togt;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.etape.CreateEtape;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.note.NoteList;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

public class TogtList extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private TogtListAdapter adapter;
    private List<Togt> togtList;
    private AppDatabase db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.togt_activity_list);

        boolean EMULATOR = Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator");
        if (!EMULATOR) {
            Sentry.init("https://99007285c4ed4ad9baa4d5ceb094e365@sentry.io/1890062", new AndroidSentryClientFactory(this));
        }

        // tempoary use
        //TODO delete
        Sentry.init("https://99007285c4ed4ad9baa4d5ceb094e365@sentry.io/1890062", new AndroidSentryClientFactory(this));

        db = DatabaseBuilder.get(this);

        recyclerView = findViewById(R.id.journeyRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
		updateList();

        findViewById(R.id.newTogtButton).setOnClickListener(this);
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
		    Executors.newSingleThreadExecutor().execute(() ->{
                        if (db.etapeDAO().getAllByTogtId(togtList.get(position).getTogt_id()).size() == 0){
                            Intent newEtapeIntent = new Intent(TogtList.this, CreateEtape.class);
                            newEtapeIntent.putExtra("togt_id", togtList.get(position).getTogt_id());
                            newEtapeIntent.putExtra("from_togt",1);
                            startActivityForResult(newEtapeIntent, 1);

                        }else{
                            Intent noteList = new Intent(this, NoteList.class);
                            noteList.putExtra("togt_id", togtList.get(position).getTogt_id());
                            startActivity(noteList);
                        }
                    });
//                Intent noteList = new Intent(this, NoteList.class);
//                noteList.putExtra("togt_id", togtList.get(position).getTogt_id());
//                startActivity(noteList);

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
