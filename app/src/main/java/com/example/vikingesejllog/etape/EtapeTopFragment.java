package com.example.vikingesejllog.etape;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.EtapeWithNotes;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

public class EtapeTopFragment extends Fragment {
	private List<EtapeWithNotes> etapeList;
	private List<String> etapeStringList;
	private ArrayAdapter adapter;
	private UpdateEtapeTopFrag callback;
	private Spinner spinner;
	
	public EtapeTopFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.etape_fragment_top, container, false);
		etapeList = new ArrayList<>();
		etapeStringList = new ArrayList<>();
		
		adapter = new ArrayAdapter(getContext(), R.layout.etape_spinner_list_elements, R.id.departure, etapeStringList) {
			@Override
			public View getView(int position, View cachedView, ViewGroup parent) {
				View view = super.getView(position, cachedView, parent);
				
				TextView departure = view.findViewById(R.id.departure);
				String s = etapeList.get(position).getEtape().getStart() + " -\n" + etapeList.get(position).getEtape().getEnd();
				departure.setText(s);
				return view;
			}
		};

		spinner = view.findViewById(R.id.spinner);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				callback.onSpinnerItemSelected(position);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			
			}
		});
		return view;
	}
	
	private void setDestination(String from, String to) {
		TextView fromTV = getView().findViewById(R.id.etape_from);
		TextView toTV = getView().findViewById(R.id.etape_to);
		fromTV.setText(from);
		toTV.setText(to);
	}
	
	private void setTime(Date date) {
		TextView tv = getView().findViewById(R.id.etape_date);
		
		if (date.getTime() != 0L) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			String s = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
			tv.setText(s);
		} else {
			tv.setText("");
		}
	}
	
	public void updateSpinner(List<EtapeWithNotes> newEtapeList) {
		System.out.println("Update spinner.");
		etapeList = newEtapeList;
		etapeStringList = new ArrayList<>();
		for (int i = 0; i < etapeList.size(); i++)
			etapeStringList.add(etapeList.get(i).etape.getStart() + " - " + etapeList.get(i).etape.getEnd());

		adapter.clear();
		adapter.addAll(etapeStringList);
		adapter.notifyDataSetChanged();
		//spinner.setSelection(etapeStringList.size() - 1, true);
	}
	
	public void setEtape(int position) {
		if (spinner != null) {
			spinner.setSelection(position);
		}
	}
	
	public void setAll(EtapeWithNotes etape, int position) {
		//setCrew(etape.etape.getSkipper(), etape.etape.getCrew().size());
		setDestination(etape.etape.getStart(), etape.etape.getEnd());
		setTime(etape.etape.getDeparture());
		setEtape(position);
	}
	
	public interface UpdateEtapeTopFrag {
		void onSpinnerItemSelected(int position);
	}
	
	public void setUpdateEtapeTopFrag(UpdateEtapeTopFrag callback) {
		this.callback = callback;
	}

	public Spinner getSpinner() {
		return spinner;
	}
}
