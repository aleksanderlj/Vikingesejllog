package com.example.vikingesejllog.etape;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;

import java.util.List;

public class CrewListAdapter extends RecyclerView.Adapter<CrewListAdapter.CrewViewHolder> {

    private List<CrewListItem> crewList;
    private Context context;
    private OnItemClickListener listener;

    public CrewListAdapter(List<CrewListItem> crewList, Context context) {
        this.crewList = crewList;
        this.context = context;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public CrewListItem getCrewlistAt(int position){
        return crewList.get(position);
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_crewitem, parent,false);
        return new CrewViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {

        CrewListItem currentCrewListItem = crewList.get(position);
        holder.crewMember.setText(currentCrewListItem.getCrewMember());
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }


    public class CrewViewHolder extends RecyclerView.ViewHolder{
        private TextView crewMember;

        public CrewViewHolder(@NonNull View itemView) {
            super(itemView);

            crewMember = (TextView) itemView.findViewById(R.id.crewMember);

        }
    }


}
