package com.example.vikingesejllog.test;

import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Note;
import com.example.vikingesejllog.model.Togt;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class TestData {
    public static ArrayList<Togt> togter;

    public static void createTestData(){
        ArrayList<String> crew = new ArrayList<>();
        crew.add("Jonabob");
        crew.add("Aleks");
        crew.add("Nicki Minaj");
        crew.add("Freddy Fazbear");
        crew.add("Androgles");
        crew.add("Det skal være Max");

        ArrayList<Note> notes = new ArrayList<Note>();
        notes.add(new Note("1.2.1", "88", "22", "19:00", 7, "9", "12", "922"));
        notes.add(new Note("1.8.4", "44", "12", "15:00", 3, "3", "21", "41"));
        notes.add(new Note("1.5.1", "51", "65", "12:44", 12, "4", "16", "221"));



        togter = new ArrayList<>();

        togter.add(new Togt("Lyngby", "Ballerup"));
        togter.add(new Togt("Danmark", "Kloster"));
        togter.add(new Togt("Aarhus", "København"));

        ArrayList<Etape> etaper = new ArrayList<>();

        Etape e1 = new Etape();
        e1.setSkipper("Troels");
        e1.setCrew(new ArrayList<>(crew));
        e1.setNoteList(new ArrayList<>(notes));
        e1.setStart("Lyngby");
        e1.setEnd("Herlev");
        e1.setDeparture(new Date());

        etaper.add(e1);

        Etape e2 = new Etape();
        e2.setSkipper("Jonatan");
        e2.setCrew(new ArrayList<>(crew));
        e2.setNoteList(new ArrayList<>(notes));
        e2.setStart("Stockholm");
        e2.setEnd("Oslo");
        e2.setDeparture(new Date());

        etaper.add(e2);

        Etape e3 = new Etape();
        e3.setSkipper("Morten");
        e3.setCrew(new ArrayList<>(crew));
        e3.setNoteList(new ArrayList<>(notes));
        e3.setStart("Brøndby");
        e3.setEnd("København");
        e3.setDeparture(new Date());

        etaper.add(e3);

        togter.get(1).setEtapeList(new ArrayList<>(etaper));
        togter.get(2).setEtapeList(new ArrayList<>(etaper));
        togter.get(3).setEtapeList(new ArrayList<>(etaper));
    }

    public static ArrayList<Togt> getTogter() {
        return togter;
    }
}
