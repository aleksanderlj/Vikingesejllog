package com.example.vikingesejllog.togt;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CreateTogt extends AppCompatActivity implements View.OnClickListener {
	private TextView departure, destination;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.togt_activity_createtogt);
		departure = findViewById(R.id.departure);
		destination = findViewById(R.id.destination);
		findViewById(R.id.godkend).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Togt togt;
		togt = new Togt(departure.getText().toString(), destination.getText().toString());
		AppDatabase db = DatabaseBuilder.get(this);
		Executors.newSingleThreadExecutor().execute(() -> {db.togtDAO().insert(togt);
            System.out.println("Hllao");
        });
		this.finish();
	}
}
