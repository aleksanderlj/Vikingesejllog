package com.example.vikingesejllog.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    private ImageView pencil, camera, mic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.note_list_fragment, container, false);

       recyclerView = (RecyclerView) view.findViewById(R.id.noteRecyclerView);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        pencil = (ImageView)getActivity().findViewById(R.id.notePencil);
        camera = (ImageView)getActivity().findViewById(R.id.noteCamera);
        mic = (ImageView)getActivity().findViewById(R.id.noteMic);

        noteListItems = new ArrayList<>();

        // Test list items
        for (int i = 0; i<=10; i++){
            NoteListItem noteListItem = new NoteListItem("Note: " + (i+1),
                    "28/10-2019\n09:13", R.drawable.pencil_black, R.drawable.camera_black, R.drawable.mic_black);
            noteListItems.add(noteListItem);
        }

        adapter = new NoteListAdapter(noteListItems, getActivity());
        recyclerView.setAdapter(adapter);


        return view;
    }
}
