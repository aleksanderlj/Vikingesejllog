package com.example.vikingesejllog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TopMenu extends Fragment {
    public TopMenu(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    public void updateTextView(String text){
        TextView tv = getView().findViewById(R.id.currentMenuText);
        tv.setText(text);
    }
}
