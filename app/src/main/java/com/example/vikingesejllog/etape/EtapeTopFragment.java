package com.example.vikingesejllog.etape;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.EtapeWithNotes;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class EtapeTopFragment extends Fragment {
    public EtapeTopFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.etape_fragment_top, container, false);
    }

    /*
    public void setCrew(String skipper, int crew){
        TextView tv = getView().findViewById(R.id.crew);
        String s = "Skipper: " + skipper + "\nBesætning: " + crew;
        tv.setText(s);
    }

     */

    public void setDestination(String from, String to){
        TextView fromTV = getView().findViewById(R.id.etape_from);
        TextView toTV = getView().findViewById(R.id.etape_to);
        fromTV.setText(from);
        toTV.setText(to);
    }

    public void setTime(Date date){

        if(date.getTime() != 0L){
            TextView tv = getView().findViewById(R.id.etape_date);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            String s = c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.YEAR);
            tv.setText(s);
        }
    }

    public void setEtape(int id, int max){
        //TextView tv = getView().findViewById(R.id.etape_nr);
        String s = "" + (id+1) + "/" + max;
        //tv.setText(s);
    }

    public void setAll(EtapeWithNotes etape){
        //setCrew(etape.etape.getSkipper(), etape.etape.getCrew().size());
        setDestination(etape.etape.getStart(), etape.etape.getEnd());
        setTime(etape.etape.getDeparture());
        //setEtape(id, max);
    }
}