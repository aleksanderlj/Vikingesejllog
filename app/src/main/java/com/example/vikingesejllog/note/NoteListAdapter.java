package com.example.vikingesejllog.note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Note;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private List<Note> noteListItems;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public NoteListAdapter(List<Note> noteListItems, Context context) {
        this.noteListItems = noteListItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_noteitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note noteListItem = noteListItems.get(position);
        holder.noteName.setText(noteListItem.getTime());
        holder.noteDate.setText(noteListItem.getTime());
        holder.pencil.setImageResource(noteListItem.getPencilImageResource());
        holder.camera.setImageResource(noteListItem.getCameraImageResource());
        holder.mic.setImageResource(noteListItem.getMicImageResource());
    }

    @Override
    public int getItemCount() {
        return noteListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView noteName;
        public TextView noteDate;
        public ImageView pencil;
        public ImageView camera;
        public ImageView mic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteName = (TextView) itemView.findViewById(R.id.noteName);
            noteDate = (TextView) itemView.findViewById(R.id.noteDate);
            pencil = (ImageView) itemView.findViewById(R.id.notePencil);
            camera = (ImageView) itemView.findViewById(R.id.noteCamera);
            mic = (ImageView) itemView.findViewById(R.id.noteMic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
