package com.example.vikingesejllog.model;

import java.util.ArrayList;
import java.util.List;

public class Togt {
    private ArrayList<Etape> etapeList;
    private String start;
    private String end;
    
    public Togt(String start, String end){
        this.start = start;
        this.end = end;
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
    
    public ArrayList<Etape> getEtapeList() {
        return etapeList;
    }
    
    public void setEtapeList(ArrayList<Etape> etapeList) {
        this.etapeList = etapeList;
    }
}
