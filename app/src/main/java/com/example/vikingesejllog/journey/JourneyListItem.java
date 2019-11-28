package com.example.vikingesejllog.journey;

public class JourneyListItem {
    private String departureDestination;
    private String date;

    public JourneyListItem(String afgangDestination, String date) {
        this.departureDestination = afgangDestination;
        this.date = date;
    }

    public String getDepartureDestination() {
        return departureDestination;
    }

    public void setDepartureDestination(String departureDestination) {
        this.departureDestination = departureDestination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
