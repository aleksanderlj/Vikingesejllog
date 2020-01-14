package com.example.vikingesejllog.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.vikingesejllog.R;

@Entity
public class Note {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private long note_id;

    @ColumnInfo(name = "etape_parent_id")
    private long etape_id;

    @ColumnInfo(name = "gps_loc")
    private String gpsLoc;

    @ColumnInfo(name = "wind_speed")
    private String windSpeed;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "rowers")
    private String rowers;

    @ColumnInfo(name = "sail_foring")
    private String sailForing;

    @ColumnInfo(name = "sail_stilling")
    private String sailStilling;

    @ColumnInfo(name = "course")
    private String course;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "pencil_image")
    private int pencilImageResource;

    @ColumnInfo(name = "camera_image")
    private int cameraImageResource;

    @ColumnInfo(name = "mic_image")
    private int micImageResource;

    @ColumnInfo(name = "media_filename")
    private String mediaFilename;

    public Note(){}

    @Ignore
    public Note(long etape_id, String gpsLoc, String windSpeed, String time, String rowers, String sailForing, String sailStilling, String course, String comment, String mediaFilename) {
        this.etape_id = etape_id;
        this.gpsLoc = gpsLoc;
        this.windSpeed = windSpeed;
        this.time = time;
        this.rowers = rowers;
        this.sailForing = sailForing;
        this.sailStilling = sailStilling;
        this.course = course;
        this.comment = comment;
        this.pencilImageResource = R.drawable.pencil_black;
        this.cameraImageResource = R.drawable.camera_black;
        this.micImageResource = R.drawable.mic_black;
        this.mediaFilename = mediaFilename;
    }

    public String getGpsLoc() {
        return gpsLoc;
    }

    public void setGpsLoc(String gpsLoc) {
        this.gpsLoc = gpsLoc;
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

    public int getPencilImageResource() {
        return pencilImageResource;
    }

    public int getCameraImageResource() {
        return cameraImageResource;
    }

    public int getMicImageResource() {
        return micImageResource;
    }

    public void setPencilImageResource(int pencilImageResource) {
        this.pencilImageResource = pencilImageResource;
    }

    public void setCameraImageResource(int cameraImageResource) {
        this.cameraImageResource = cameraImageResource;
    }

    public void setMicImageResource(int micImageResource) {
        this.micImageResource = micImageResource;
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
            this.cameraImageResource = R.drawable.camera_black;
        }
    }
    public void hideMic(boolean hidden){
        if(hidden == true){
            this.micImageResource = 0;
        } else {
            this.micImageResource = R.drawable.mic_black;
        }
    }

    public long getNote_id() {
        return note_id;
    }

    public void setNote_id(long note_id) {
        this.note_id = note_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getEtape_id() {
        return etape_id;
    }

    public void setEtape_id(long etape_id) {
        this.etape_id = etape_id;
    }

    public String getMediaFilename() {
        return mediaFilename;
    }

    public void setMediaFilename(String mediaFilename) {
        this.mediaFilename = mediaFilename;
    }
}

