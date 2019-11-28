package com.example.vikingesejllog.model;

import java.util.ArrayList;
import java.util.Date;

public class Etape {
    private String skipper;
    private ArrayList<String> crew;
    private ArrayList<Note> noteList;
    private String start;
    private String end;
    private Date departure;

    public Etape(){
        crew = new ArrayList<>();
        noteList = new ArrayList<>();
    }

    public String getSkipper() {
        return skipper;
    }

    public void setSkipper(String skipper) {
        this.skipper = skipper;
    }

    public ArrayList<String> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<String> crew) {
        this.crew = crew;
    }

    public ArrayList<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(ArrayList<Note> noteList) {
        this.noteList = noteList;
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
}
