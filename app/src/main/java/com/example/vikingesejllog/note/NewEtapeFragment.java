package com.example.vikingesejllog.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Etape;
import com.google.gson.Gson;


public class NewEtapeFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.new_etape_fragment, container, false);
        view.findViewById(R.id.new_etape).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.new_etape:
                Intent i = new Intent(getActivity(), NewEtape.class);
                getActivity().startActivityForResult(i, 1);
                break;
        }
    }
}
