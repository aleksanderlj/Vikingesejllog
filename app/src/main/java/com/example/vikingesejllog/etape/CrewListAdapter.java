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

public class CrewListAdapter extends RecyclerView.Adapter<CrewListAdapter.ViewHolder> {

    private List<CrewListItem> crewList;
    private Context context;

    public CrewListAdapter(List<CrewListItem> crewList, Context context) {
        this.crewList = crewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_crewitem, parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CrewListItem crewListItem = crewList.get(position);

        holder.crewMember.setText(crewListItem.getCrewMember());
    }

    @Override
    public int getItemCount() {

        return crewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView crewMember;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            crewMember = (TextView) itemView.findViewById(R.id.crewMember);

        }
    }
}
