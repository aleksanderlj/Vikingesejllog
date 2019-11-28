package com.example.vikingesejllog.note;

import com.example.vikingesejllog.R;

public class NoteListItem {

    private String noteName;
    private String noteDate;
    private int pencilImageResource;
    private int cameraImageResource;
    private int micImageResource;

    public NoteListItem(String noteName, String noteDate) {
        this.noteName = noteName;
        this.noteDate = noteDate;
        this.pencilImageResource = R.drawable.pencil_black;
        this.cameraImageResource = R.drawable.camera_black;
        this.micImageResource = R.drawable.mic_black;
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
