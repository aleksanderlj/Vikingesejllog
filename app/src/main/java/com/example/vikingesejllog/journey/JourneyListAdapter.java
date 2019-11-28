package com.example.vikingesejllog.journey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.R;

import java.util.List;

public class JourneyListAdapter extends RecyclerView.Adapter<JourneyListAdapter.ViewHolder> {

    private List<JourneyListItem> listItems;
    private Context context;

    public JourneyListAdapter(List<JourneyListItem> listItems, Context context) {
        this.listItems = listItems;
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
        JourneyListItem journeyListItem = listItems.get(position);

        holder.departureDestination.setText(journeyListItem.getDepartureDestination());
        holder.journeyDate.setText(journeyListItem.getDate());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView departureDestination;
        public TextView journeyDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            departureDestination = (TextView) itemView.findViewById(R.id.departureDestination);
            journeyDate = (TextView) itemView.findViewById(R.id.journeyDate);
        }
    }
}
