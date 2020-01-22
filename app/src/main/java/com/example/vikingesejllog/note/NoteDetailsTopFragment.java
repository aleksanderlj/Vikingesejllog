package com.example.vikingesejllog.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.vikingesejllog.R;

public class NoteDetailsTopFragment extends Fragment {
    public NoteDetailsTopFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_fragment_notedetails_top, container, false);
    }

    public void updateTextView(String text){
        TextView tv = getView().findViewById(R.id.noteFragmentMenuText);
        tv.setText(text);
    }
}
