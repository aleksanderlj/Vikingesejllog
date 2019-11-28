package com.example.vikingesejllog.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;

import java.util.ArrayList;
import java.util.List;

public class NoteListFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<NoteListItem> noteListItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.note_list_fragment, container, false);

       recyclerView = (RecyclerView) view.findViewById(R.id.noteRecyclerView);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        noteListItems = new ArrayList<>();

        int pencilID = getResources().getIdentifier("notePencil","drawable", getActivity().getPackageName());
        int cameraID = getResources().getIdentifier("noteCamera","drawable", getActivity().getPackageName());
        int micID = getResources().getIdentifier("noteMic","drawable", getActivity().getPackageName());

        // Test list items
        for (int i = 0; i<=10; i++){
            NoteListItem noteListItem = new NoteListItem("Note " + (i+1),
                    "28/10-2019 09:13", pencilID, cameraID, micID);
            noteListItems.add(noteListItem);
        }

        adapter = new NoteListAdapter(noteListItems, getActivity());
        recyclerView.setAdapter(adapter);


        return view;
    }
}
