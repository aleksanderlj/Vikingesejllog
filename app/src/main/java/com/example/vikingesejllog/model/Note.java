package com.example.vikingesejllog.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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

    @ColumnInfo(name = "contains_comment")
    private boolean hasComment;

    @ColumnInfo(name = "contains_image")
    private boolean hasImage;

    @ColumnInfo(name = "contains_audio")
    private boolean hasAudio;

    @ColumnInfo(name = "media_filename")
    private String fileName;

    public Note(){}

    @Ignore
    public Note(long etape_id, String gpsLoc, String windSpeed, String time, String rowers, String sailForing, String sailStilling, String course, String comment, boolean hasComment, boolean hasImage, boolean hasAudio, String fileName) {
        this.etape_id = etape_id;
        this.gpsLoc = gpsLoc;
        this.windSpeed = windSpeed;
        this.time = time;
        this.rowers = rowers;
        this.sailForing = sailForing;
        this.sailStilling = sailStilling;
        this.course = course;
        this.comment = comment;
        this.hasComment = hasComment;
        this.hasImage = hasImage;
        this.hasAudio = hasAudio;
        this.fileName = fileName;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isHasComment() {
        return hasComment;
    }

    public void setHasComment(boolean hasComment) {
        this.hasComment = hasComment;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public boolean isHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(boolean hasAudio) {
        this.hasAudio = hasAudio;
    }

    public static Note GetEmptyNote(){
        Note n = new Note();
        n.setEtape_id(-1);
        n.setGpsLoc("");
        n.setWindSpeed("");
        n.setTime("");
        n.setRowers("");
        n.setSailForing("");
        n.setSailStilling("");
        n.setCourse("");
        n.setComment("");
        n.setHasComment(false);
        n.setHasImage(false);
        n.setHasAudio(false);
        n.setFileName("");
        return n;
    }
}

