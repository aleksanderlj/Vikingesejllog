package com.example.vikingesejllog.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class EtapeWithNotes {

    @Embedded
    public Etape etape;

    @Relation(parentColumn = "etape_id", entityColumn = "etape_parent_id", entity = Note.class)
    public List<Note> noteList;

    public Etape getEtape() {
        return etape;
    }

    public void setEtape(Etape etape) {
        this.etape = etape;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }
}
