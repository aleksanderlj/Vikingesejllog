package com.example.vikingesejllog.journey;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Togt;
import com.google.gson.Gson;

public class NewJourney extends AppCompatActivity implements View.OnClickListener {
	private TextView departure, destination;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_journey);
		departure = findViewById(R.id.departure);
		destination = findViewById(R.id.destination);
		findViewById(R.id.godkend).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Togt togt;
		String startPoint, endPoint;
		startPoint = departure.getText().toString();
		endPoint = destination.getText().toString();
		togt = new Togt(startPoint, endPoint);
		
		Gson gson = new Gson();
		String jsonTogt = gson.toJson(togt);
		SharedPreferences prefs = getSharedPreferences("togtList", MODE_PRIVATE);
		SharedPreferences.Editor editor = getSharedPreferences("togtList", MODE_PRIVATE).edit();
		editor.putString(Integer.toString(prefs.getAll().size() + 1), jsonTogt);
		editor.apply();
		
		this.finish();
	}
}
