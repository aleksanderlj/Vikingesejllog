package com.example.vikingesejllog.test;

import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Togt;

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



        togter = new ArrayList<>();

        togter.add(new Togt("Lyngby", "Ballerup"));
        togter.add(new Togt("Danmark", "Kloster"));
        togter.add(new Togt("Aarhus", "København"));

        ArrayList<Etape> etaper = new ArrayList<>();

        Etape e1 = new Etape();
        e1.setSkipper("Troels");
        e1.setCrew(crew);
        e1.setStart("Lyngby");
        e1.setEnd("Herlev");
        e1.setDeparture(new Date());

        etaper.add(e1);

        Etape e2 = new Etape();
        Etape e3 = new Etape();

        ArrayList<Etape> etaper2 = new ArrayList<>();
        ArrayList<Etape> etaper3 = new ArrayList<>();
    }
}
