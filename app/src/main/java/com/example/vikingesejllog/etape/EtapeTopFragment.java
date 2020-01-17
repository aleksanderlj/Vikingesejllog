package com.example.vikingesejllog.etape;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.room.Update;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class EtapeTopFragment extends Fragment {
    private AppDatabase db;
    private List<EtapeWithNotes> etapeList;
    private ArrayAdapter adapter;
    private UpdateEtapeTopFrag callback;
    private int currPosition;
    private long togt_id;
    private Togt togt;
    private Spinner spinner;
    
    public EtapeTopFragment(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.etape_fragment_top, container, false);
        db = DatabaseBuilder.get(getContext());
        etapeList = new ArrayList<>();
        
        //adapter.setlist(etapeList);
        
        Executors.newSingleThreadExecutor().execute(()->{
            List<EtapeWithNotes> newList = db.etapeDAO().getAllByTogtId(togt_id);
            //List<EtapeWithNotes> newList = db.etapeDAO().getAllByTogtId();
            etapeList.clear();
            etapeList.addAll(newList);
            //adapter.notifyDataSetChanged();
        });
        spinner = view.findViewById(R.id.destination);
        String[] etapeDeparture = new String[etapeList.size()];
        for (int i = 0; i < etapeDeparture.length; i++)
            etapeDeparture[i] = etapeList.get(i).etape.getStart() + " - " + etapeList.get(i).etape.getEnd();
        
        adapter = new ArrayAdapter(getContext(), R.layout.etape_spinner_list_elements, R.id.departure, etapeDeparture){
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
        spinner.setAdapter(adapter);
        spinner.setSelection(currPosition);
        
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
    
    public void setCrew(String skipper, int crew){
        TextView tv = getView().findViewById(R.id.crew);
        String s = "Skipper: " + skipper + "\nBesætning: " + crew;
        tv.setText(s);
    }

    public void setTogt(long togt_id){
        this.togt_id = togt_id;
    }
    
    public void setEtape(int position){
        this.currPosition = position;
        if (spinner != null)
            spinner.setSelection(position);
    }
    
    public void setAll(EtapeWithNotes etape, int currPosition){
        setCrew(etape.etape.getSkipper(), etape.etape.getCrew().size());
        //setDestination(etape.etape.getStart(), etape.etape.getEnd(), etapeList);
        setEtape(currPosition);
    }
    
    public interface UpdateEtapeTopFrag{
        void onSpinnerItemSelected(int position);
        
    }
    
    public void setUpdateEtapeTopFrag(UpdateEtapeTopFrag callback){
        this.callback = callback;
    }
}
