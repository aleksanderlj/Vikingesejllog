package com.example.vikingesejllog.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TogtWithEtapes {

    @Embedded
    public Togt togt;

    @Relation(parentColumn = "togt_id", entityColumn = "togt_parent_id", entity = Etape.class)
    public List<Etape> etapeList;

    public Togt getTogt() {
        return togt;
    }

    public void setTogt(Togt togt) {
        this.togt = togt;
    }

    public List<Etape> getEtapeList() {
        return etapeList;
    }

    public void setEtapeList(List<Etape> etapeList) {
        this.etapeList = etapeList;
    }
}
