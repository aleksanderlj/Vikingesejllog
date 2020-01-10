package com.example.vikingesejllog.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.util.ArrayList;
import java.util.List;

public class NoteListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private NoteListAdapter adapter;
    private EtapeWithNotes etape;

    public NoteListFragment() {
    }

    public NoteListFragment(EtapeWithNotes etape) {
        this.etape = etape;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.note_fragment_notelist, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.noteRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new NoteListAdapter(etape.getNoteList(), getActivity());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                        // Følgende kode henter variable fra note objektet der trykkes på, og sender dem til en NoteDetails aktivitet.
                        Intent intent = new Intent(getActivity(), NoteDetails.class);
                        Note note = etape.getNoteList().get(position);
                        if (note.getBoatSpeed() != null){
                            intent.putExtra("boatSpeed", note.getBoatSpeed());
                        }
                        if (note.getWindSpeed() != null){
                            intent.putExtra("windSpeed", note.getWindSpeed());
                        }
                        if (note.getGpsLoc() != null){
                            intent.putExtra("gpsLoc", note.getGpsLoc());
                        }
                        if (note.getTime() != null){
                            intent.putExtra("time", note.getTime());
                        }
                        if (note.getRowers() != null){
                            intent.putExtra("rowers", note.getRowers());
                        }
                        if (note.getSailForing() != null){
                            intent.putExtra("sailForing", note.getSailForing());
                        }
                        if (note.getSailStilling() != null){
                            intent.putExtra("sailStilling", note.getSailStilling());
                        }
                        if (note.getCourse() != null){
                        intent.putExtra("course", note.getCourse());
                        }
                        if (note.getComment() != null){
                        intent.putExtra("comment", note.getComment());
                        }
                        intent.putExtra("noteNumber", (position+1));
                        intent.putExtra("noteCount", etape.getNoteList().size());
                startActivity(intent);
            }
        });

        view.findViewById(R.id.newHarborButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newHarborButton:
                Intent i = new Intent(getActivity(), CreateNote.class);
                i.putExtra("etape_id", etape.etape.getEtape_id());
                getActivity().startActivityForResult(i, 2);
                break;
        }
    }
}
