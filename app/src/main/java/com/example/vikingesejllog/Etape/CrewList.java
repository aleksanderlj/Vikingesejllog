package com.example.vikingesejllog.Etape;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;

public class CrewList extends AppCompatActivity {

    //private List<CrewListItem> crewListItems;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crew_list);

        //crewListItems = new ArrayList<>();


    }
}
