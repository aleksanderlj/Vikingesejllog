package com.example.vikingesejllog.togt;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.other.DatabaseBuilder;

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
		db.togtDAO().insert(togt);
		this.finish();
	}
}
