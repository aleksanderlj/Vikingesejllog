package com.example.vikingesejllog;

public class Note {

    private String gpsLoc;
    private String boatSpeed;
    private String windSpeed;
    private String time;
    private String rowers;
    private String sailForing;
    private String sailStilling;
    private String course;
    private String comment;

    public Note(String gpsLoc, String boatSpeed, String windSpeed, String time, String rowers, String sailForing, String sailStilling, String course, String comment) {
        this.gpsLoc = gpsLoc;
        this.boatSpeed = boatSpeed;
        this.windSpeed = windSpeed;
        this.time = time;
        this.rowers = rowers;
        this.sailForing = sailForing;
        this.sailStilling = sailStilling;
        this.course = course;
        this.comment = comment;

    }

    public String getGpsLoc() {
        return gpsLoc;
    }

    public void setGpsLoc(String gpsLoc) {
        this.gpsLoc = gpsLoc;
    }

    public String getBoatSpeed() {
        return boatSpeed;
    }

    public void setBoatSpeed(String boatSpeed) {
        this.boatSpeed = boatSpeed;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRowers() {
        return rowers;
    }

    public void setRowers(String rowers) {
        this.rowers = rowers;
    }

    public String getSailForing() {
        return sailForing;
    }

    public void setSailForing(String sailForing) {
        this.sailForing = sailForing;
    }

    public String getSailStilling() {
        return sailStilling;
    }

    public void setSailStilling(String sailStilling) {
        this.sailStilling = sailStilling;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

