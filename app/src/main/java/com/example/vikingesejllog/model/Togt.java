package com.example.vikingesejllog.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Togt {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "togt_id")
    private long togt_id;

    @ColumnInfo(name = "departure")
    private String departure;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "departureDate")
    private Date departureDate;

    public Togt(){}

    @Ignore
    public Togt(String departure, String destination){
        this.departure = departure;
        this.name = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public long getTogt_id() {
        return togt_id;
    }

    public void setTogt_id(long togt_id) {
        this.togt_id = togt_id;
    }
}
