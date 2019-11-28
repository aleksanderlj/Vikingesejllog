package com.example.vikingesejllog.test;

import com.example.vikingesejllog.model.Etape;
import com.example.vikingesejllog.model.Togt;

import java.util.ArrayList;

public class TestData {
    public static ArrayList<Togt> togter;

    public static void createTestData(){
        togter = new ArrayList<Togt>();

        togter.add(new Togt("Lyngby", "Ballerup"));
        togter.add(new Togt("Danmark", "Kloster"));
        togter.add(new Togt("Aarhus", "København"));

        ArrayList<Etape> etaper1 = new ArrayList<>();
        ArrayList<Etape> etaper2 = new ArrayList<>();
        ArrayList<Etape> etaper3 = new ArrayList<>();
    }
}
