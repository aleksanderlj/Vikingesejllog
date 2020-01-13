package com.example.vikingesejllog.etape;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.vikingesejllog.R;

public class CrewListFragment extends Fragment {

    public CrewListFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.addcrew_fragment, container,false);
    }

    public void setCrewName(String crewName){
        TextView textView = getView().findViewById(R.id.crewMemberName);

        //textView.setText();
    }
}


// bla bla