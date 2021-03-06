package com.example.vikingesejllog.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Etape {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "etape_id")
    private long etape_id;

    @ColumnInfo(name = "togt_parent_id")
    private long togt_id;

    @ColumnInfo(name = "skipper")
    private String skipper;

    @ColumnInfo(name = "crew")
    private List<String> crew;

    @ColumnInfo(name = "start")
    private String start;

    @ColumnInfo(name = "end")
    private String end;

    @ColumnInfo(name = "departure")
    private Date departure;

    public Etape(){
        crew = new ArrayList<>();
    }

    public String getSkipper() {
        return skipper;
    }

    public void setSkipper(String skipper) {
        this.skipper = skipper;
    }

    public List<String> getCrew() {
        return crew;
    }

    public void setCrew(List<String> crew) {
        this.crew = crew;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public long getEtape_id() {
        return etape_id;
    }

    public void setEtape_id(long etape_id) {
        this.etape_id = etape_id;
    }

    public long getTogt_id() {
        return togt_id;
    }

    public void setTogt_id(long togt_id) {
        this.togt_id = togt_id;
    }
}
