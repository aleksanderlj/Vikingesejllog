package com.example.vikingesejllog.model;

import com.example.vikingesejllog.R;

public class Note {

    private String gpsLoc;
    private String boatSpeed;
    private String windSpeed;
    private String time;
    private int rowers;
    private String sailForing;
    private String sailStilling;
    private String course;
    private int pencilImageResource;
    private int cameraImageResource;
    private int micImageResource;

    public Note(String gpsLoc, String boatSpeed, String windSpeed, String time, int rowers, String sailForing, String sailStilling, String course) {
        this.gpsLoc = gpsLoc;
        this.boatSpeed = boatSpeed;
        this.windSpeed = windSpeed;
        this.time = time;
        this.rowers = rowers;
        this.sailForing = sailForing;
        this.sailStilling = sailStilling;
        this.course = course;
        this.pencilImageResource = R.drawable.pencil_black;
        this.cameraImageResource = R.drawable.camera_black;
        this.micImageResource = R.drawable.mic_black;
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

    public int getRowers() {
        return rowers;
    }

    public void setRowers(int rowers) {
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

    public int getPencilImageResource() {
        return pencilImageResource;
    }

    public int getCameraImageResource() {
        return cameraImageResource;
    }

    public int getMicImageResource() {
        return micImageResource;
    }

    public void hidePencil(boolean hidden){
        if(hidden == true){
            this.pencilImageResource = 0;
        } else {
            this.pencilImageResource = R.drawable.pencil_black;
        }
    }
    public void hideCamera(boolean hidden){
        if(hidden == true){
            this.cameraImageResource = 0;
        } else {
            this.cameraImageResource = R.drawable.pencil_black;
        }
    }
    public void hideMic(boolean hidden){
        if(hidden == true){
            this.micImageResource = 0;
        } else {
            this.micImageResource = R.drawable.pencil_black;
        }
    }
}

