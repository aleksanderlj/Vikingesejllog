package com.example.vikingesejllog.togt;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;

public class CreateTogt extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
	private TextView departure, name;
	Date departureDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.togt_activity_createtogt);
		departureDate = new Date(0L);

		departure = findViewById(R.id.createTogtDepartureEditText);
		name = findViewById(R.id.createTogtNameEditText);
		findViewById(R.id.createTogtDepartureDateBox).setOnClickListener(this);
		findViewById(R.id.createTogtAccepterBtn).setOnClickListener(this);
		findViewById(R.id.createTogtAfbrydBtn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.createTogtAccepterBtn:
				Togt togt;
				togt = new Togt(departure.getText().toString(), name.getText().toString());
				togt.setDepartureDate(departureDate);
				AppDatabase db = DatabaseBuilder.get(this);
				Executors.newSingleThreadExecutor().execute(() -> {
					db.togtDAO().insert(togt);
					setResult(Activity.RESULT_OK);
					finish();
				});
				break;
			case R.id.createTogtAfbrydBtn:
				setResult(Activity.RESULT_CANCELED);
				finish();
				break;
			case R.id.createTogtDepartureDateBox:
				Calendar c = Calendar.getInstance();
				DatePickerDialog dp = new DatePickerDialog(this, this,
						c.get(Calendar.YEAR),
						c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH));
				dp.show();
				break;

		}

	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, dayOfMonth);
		departureDate = c.getTime();
		String s = "" + dayOfMonth + "-" + (month+1) + "-" + year;
		TextView date = findViewById(R.id.createTogtDepartureDateText);
		date.setText(s);
	}
}