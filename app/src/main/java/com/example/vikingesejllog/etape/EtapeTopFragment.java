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
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class EtapeTopFragment extends Fragment implements OnItemSelectedListener {
    private AppDatabase db;
    private List<EtapeWithNotes> etapeList;
    private ArrayAdapter adapter;
    private UpdateEtapeTopFrag callback;
    
    public EtapeTopFragment(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.etape_fragment_top, container, false);
        db = DatabaseBuilder.get(getContext());
        etapeList = new ArrayList<>();
        
        //adapter.setlist(etapeList);
        
        Executors.newSingleThreadExecutor().execute(()->{
            List<EtapeWithNotes> newList = db.etapeDAO().getAll();
            //List<EtapeWithNotes> newList = db.etapeDAO().getAllByTogtId();
            etapeList.clear();
            etapeList.addAll(newList);
            //adapter.notifyDataSetChanged();
        });
        Spinner spinner = view.findViewById(R.id.destination);
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
        
        return view;
    }
    
    public void setCrew(String skipper, int crew){
        TextView tv = getView().findViewById(R.id.crew);
        String s = "Skipper: " + skipper + "\nBesætning: " + crew;
        tv.setText(s);
    }

    /*public void setDestination(String from, String to, List<EtapeWithNotes> etapeList){
    
    }*/
    
    public void setAll(EtapeWithNotes etape, int id, int max){
        setCrew(etape.etape.getSkipper(), etape.etape.getCrew().size());
        //setDestination(etape.etape.getStart(), etape.etape.getEnd(), etapeList);
        //setEtape(id, max);
    }
    
    public interface UpdateEtapeTopFrag{
        void onSpinnerItemSelected(int position);
    }
    
    public void setUpdateEtapeTopFrag(UpdateEtapeTopFrag callback){
        this.callback = callback;
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    
    }
    
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    
    }
}
