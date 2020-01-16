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

        holder.time.setText(noteListItem.getTime());
        bindView(noteListItem.getWindSpeed(), holder.wind, holder.wind_text);
        bindView(noteListItem.getCourse(), holder.course, holder.course_text);
        bindView(noteListItem.getRowers(), holder.rowers, holder.rowers_text);
        bindView(noteListItem.getSailStilling(), holder.sailstilling, holder.sailstilling_text);
        bindView(noteListItem.getSailForing(), holder.sailforing, holder.sailforing_text);

    }

    private void bindView(String text, TextView info, TextView title){
        if(text.compareTo("") == 0)
            title.setVisibility(View.INVISIBLE);
        info.setText(text);
    }

    @Override
    public int getItemCount() {
        return noteListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView time;
        public TextView wind;
        public TextView course;
        public TextView rowers;
        public TextView sailstilling;
        public TextView sailforing;

        public TextView wind_text;
        public TextView course_text;
        public TextView rowers_text;
        public TextView sailstilling_text;
        public TextView sailforing_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.note_time);
            wind = itemView.findViewById(R.id.note_wind);
            course = itemView.findViewById(R.id.note_course);
            rowers = itemView.findViewById(R.id.note_rowers);
            sailstilling = itemView.findViewById(R.id.note_sailstilling);
            sailforing = itemView.findViewById(R.id.note_sailforing);

            wind_text = itemView.findViewById(R.id.note_wind_text);
            course_text = itemView.findViewById(R.id.note_course_text);
            rowers_text = itemView.findViewById(R.id.note_rowers_text);
            sailstilling_text = itemView.findViewById(R.id.note_sailstilling_text);
            sailforing_text = itemView.findViewById(R.id.note_sailforing_text);

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
