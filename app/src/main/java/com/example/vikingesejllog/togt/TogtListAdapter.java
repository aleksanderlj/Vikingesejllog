package com.example.vikingesejllog.togt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vikingesejllog.AppDatabase;
import com.example.vikingesejllog.R;
import com.example.vikingesejllog.model.Togt;
import com.example.vikingesejllog.other.DatabaseBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class TogtListAdapter extends RecyclerView.Adapter<TogtListAdapter.ViewHolder> {

    private List<Togt> togtList;
    private Context context;
    private OnItemClickListener listener;
    private AppDatabase db;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public TogtListAdapter(List<Togt> togtList, Context context) {
        this.togtList = togtList;
        this.context = context;
        db = DatabaseBuilder.get(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.togt_item_togtitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Togt togt = togtList.get(position);

        holder.departure.setText(togt.getDeparture());
        holder.name.setText(togt.getName());

        Date d = togt.getDepartureDate();

        if (d.getTime() != 0L) {
            Calendar c = Calendar.getInstance();
            c.setTime(togt.getDepartureDate());
            String s = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
            holder.togtDate.setText(s);
        } else {
            holder.togtDate.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return togtList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView departure, name, togtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            departure = (TextView) itemView.findViewById(R.id.togtAfgang);
            name = (TextView) itemView.findViewById(R.id.togtName);
            togtDate = (TextView) itemView.findViewById(R.id.togtDato);

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
                    ad.setTitle("Vil du slette dette togt?");
                    ad.setPositiveButton("Godkend", (dialog, which) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            db.togtDAO().delete(togtList.get(getAdapterPosition()));
                            togtList.remove(togtList.get(getAdapterPosition()));
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
