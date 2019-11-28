package com.example.vikingesejllog.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.vikingesejllog.R;

public class EtapeTopFragment extends Fragment {
    public EtapeTopFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.etape_topfragment, container, false);
    }

    public void setCrew(String skipper, int crew){
        TextView tv = getView().findViewById(R.id.crew);
        String s = "Skipper: " + skipper + "\nBesætning: " + crew;
        tv.setText(s);
    }

    public void setDestination(String from, String to){
        TextView tv = getView().findViewById(R.id.destination);
        String s = from + " - " + to;
        tv.setText(s);
    }

    public void setEtape(int id){
        TextView tv = getView().findViewById(R.id.etape_nr);
        String s = "" + id;
        tv.setText(s);
    }
}
