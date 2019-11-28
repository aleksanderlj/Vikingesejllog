package com.example.vikingesejllog.journey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Togt;

import java.util.List;

public class JourneyListAdapter extends RecyclerView.Adapter<JourneyListAdapter.ViewHolder> {

    private List<Togt> togtList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public JourneyListAdapter(List<Togt> togtList, Context context) {
        this.togtList = togtList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Togt togt = togtList.get(position);

        holder.departure.setText(togt.getDeparture());
        holder.destination.setText(togt.getDestination());
    }

    @Override
    public int getItemCount() {
        return togtList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView departure, destination;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            departure = (TextView) itemView.findViewById(R.id.togtAfgang);
            destination = (TextView) itemView.findViewById(R.id.togtDestination);
//            togtDate = (TextView) itemView.findViewById(R.id.togtDato);

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
