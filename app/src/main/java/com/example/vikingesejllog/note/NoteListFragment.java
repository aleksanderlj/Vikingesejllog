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

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.EtapeWithNotes;
import com.example.vikingesejllog.model.Note;

import io.sentry.Sentry;

public class NoteListFragment extends Fragment {

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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.note_fragment_recyclerview, container, false);

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
                try {
                    intent.putExtra("noteId", note.getNote_id());
                    intent.putExtra("fileName", note.getFileName());
                    intent.putExtra("noteNumber", (position + 1));
                    intent.putExtra("noteCount", etape.getNoteList().size());
                } catch (Exception e){
                    Sentry.capture(e);
                    e.printStackTrace();
                }

            startActivity(intent);
        }
        });

        return view;
    }

    public NoteListAdapter getAdapter() {
        return adapter;
    }
}
