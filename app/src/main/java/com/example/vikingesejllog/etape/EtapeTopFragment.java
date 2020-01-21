package com.example.vikingesejllog.etape;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class EtapeTopFragment extends Fragment {
    private AppDatabase db;
    private List<EtapeWithNotes> etapeList;
    private List<String> etapeStringList;
    private ArrayAdapter adapter;
    private UpdateEtapeTopFrag callback;
    private int currPosition;
	private Spinner spinner;
    
    public EtapeTopFragment(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.etape_fragment_top, container, false);
        db = DatabaseBuilder.get(getContext());
        etapeList = new ArrayList<>();
        etapeStringList = new ArrayList<>();
	
		adapter = new ArrayAdapter(getContext(), R.layout.etape_spinner_list_elements, R.id.departure, etapeStringList){
			@Override
			public View getView(int position, View cachedView, ViewGroup parent){
				View view = super.getView(position, cachedView, parent);
			
				TextView departure = view.findViewById(R.id.departure);
				departure.setTextColor(Color.parseColor("#FFFFFF"));
				String s = etapeList.get(position).getEtape().getStart() + " -\n" + etapeList.get(position).getEtape().getEnd();
				departure.setText(s);
				return view;
			}
		};
		
        spinner = view.findViewById(R.id.destination);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callback.onSpinnerItemSelected(position);
                currPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
        
            }
        });
        return view;
    }
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	public void setCrew(String skipper, int crew){
        TextView tv = getView().findViewById(R.id.crew);
        String s = "Skipper: " + skipper + "\nBesætning: " + crew;
        tv.setText(s);
    }

    public void updateSpinner(long togt_id){
		System.out.println("Update spinner.");
		Executors.newSingleThreadExecutor().execute(() -> {
			etapeList.clear();
			etapeList.addAll(db.etapeDAO().getAllByTogtId(togt_id));
			//System.out.println(etapeList.toString());
			etapeStringList = new ArrayList<>();
			for (int i = 0; i < etapeList.size(); i++)
				etapeStringList.add(etapeList.get(i).etape.getStart() + " - " + etapeList.get(i).etape.getEnd());
			getActivity().runOnUiThread(()->{
				adapter.clear();
				adapter.addAll(etapeStringList);
				adapter.notifyDataSetChanged();
				spinner.setSelection(etapeStringList.size());
			});

		});
    }
    
    public void setEtape(int position){
        this.currPosition = position;
        if (spinner != null) {
			spinner.setSelection(position);
		}
    }
    
    public void setAll(EtapeWithNotes etape){
        setCrew(etape.etape.getSkipper(), etape.etape.getCrew().size());
        //setDestination(etape.etape.getStart(), etape.etape.getEnd(), etapeList);
        //setEtape(currPosition);
    }
    
    public interface UpdateEtapeTopFrag{
        void onSpinnerItemSelected(int position);
        
    }
    
    public void setUpdateEtapeTopFrag(UpdateEtapeTopFrag callback){
        this.callback = callback;
    }
}
