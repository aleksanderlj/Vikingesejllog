package com.example.vikingesejllog.note;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.util.List;
import java.util.concurrent.Executors;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private List<Note> noteListItems;
    private Context context;
    private OnItemClickListener listener;
    private AppDatabase db;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public NoteListAdapter(List<Note> noteListItems, Context context) {
        this.noteListItems = noteListItems;
        this.context = context;
        db = DatabaseBuilder.get(context);
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

        if(!noteListItem.isHasImage()){
            holder.picImage.setVisibility(View.GONE);
        } else{
            holder.picImage.setVisibility(View.VISIBLE);
        }

        if(!noteListItem.isHasAudio()){
            holder.audioImage.setVisibility(View.GONE);
        }else{
            holder.audioImage.setVisibility(View.VISIBLE);
        }

        if(!noteListItem.isHasComment()){
            holder.commentImage.setVisibility(View.GONE);
        }else{
            holder.commentImage.setVisibility(View.VISIBLE);
        }


    }

    private void bindView(String text, TextView info, TextView title){
        if(text.compareTo("") == 0)
            title.setVisibility(View.INVISIBLE);
        else
            title.setVisibility(View.VISIBLE);
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

        ImageView commentImage;
        ImageView picImage;
        ImageView audioImage;

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

            commentImage = itemView.findViewById(R.id.comment_image);
            picImage = itemView.findViewById(R.id.camera_image);
            audioImage = itemView.findViewById(R.id.audio_image);

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

            itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                MenuItem mItem = menu.add(0, v.getId(), 0, "Slet");
                mItem.setOnMenuItemClickListener((item) -> {
                    AlertDialog.Builder ad = new AlertDialog.Builder(context);
                    ad.setTitle("Vil du slette denne note?");
                    ad.setPositiveButton("Godkend", (dialog, which) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            db.noteDAO().delete(noteListItems.get(getAdapterPosition()));
                            noteListItems.remove(noteListItems.get(getAdapterPosition()));
                            ((Activity) context).runOnUiThread(() -> notifyDataSetChanged());
                        });
                    });
                    ad.setNegativeButton("Fortryd", (dialog, which) -> {
                        dialog.cancel();
                    });

                    ad.create().show();

                    return true;
                });
            });
        }
    }
}
