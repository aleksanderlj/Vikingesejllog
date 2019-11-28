package com.example.vikingesejllog.note;

public class NoteListItem {

    private String noteName;
    private String noteDate;
    private int pencilImageResource;
    private int cameraImageResource;
    private int micImageResource;

    public NoteListItem(String noteName, String noteDate, int pencilImageResource,
                        int cameraImageResource, int micImageResource) {
        this.noteName = noteName;
        this.noteDate = noteDate;
        this.pencilImageResource = pencilImageResource;
        this.cameraImageResource = cameraImageResource;
        this.micImageResource = micImageResource;
    }

    public String getNoteName() {
        return noteName;
    }

    public String getNoteDate() {
        return noteDate;
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
}
