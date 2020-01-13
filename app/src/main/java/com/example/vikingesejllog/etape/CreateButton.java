package com.example.vikingesejllog.etape;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vikingesejllog.R;


public class CreateButton extends Fragment implements View.OnClickListener {
    long togt_id;

    public CreateButton(long togt_id){
        this.togt_id = togt_id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.etape_fragment_createbutton, container, false);
        view.findViewById(R.id.new_etape).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.new_etape:
                Intent i = new Intent(getActivity(), CreateEtape.class);
                i.putExtra("togt_id", togt_id);
                getActivity().startActivityForResult(i, 1);
                break;
        }
    }
}
