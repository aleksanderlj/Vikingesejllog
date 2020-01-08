package com.example.vikingesejllog.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Togt {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "togt_id")
    private long togt_id;

    @ColumnInfo(name = "departure")
    private String departure;

    @ColumnInfo(name = "destination")
    private String destination;

    public Togt(){}

    @Ignore
    public Togt(String departure, String destination){
        this.departure = departure;
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getTogt_id() {
        return togt_id;
    }

    public void setTogt_id(long togt_id) {
        this.togt_id = togt_id;
    }
}
