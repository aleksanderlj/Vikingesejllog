package com.example.vikingesejllog.model;

import java.util.ArrayList;

public class Togt {
    private ArrayList<Etape> etapeList;
    private String departure;
    private String destination;

    public Togt(String departure, String destination){
        this.departure = departure;
        this.destination = destination;
        this.etapeList = new ArrayList<>();
    }

    public ArrayList<Etape> getEtapeList() {
        return etapeList;
    }

    public void setEtapeList(ArrayList<Etape> etapeList) {
        this.etapeList = etapeList;
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
}
