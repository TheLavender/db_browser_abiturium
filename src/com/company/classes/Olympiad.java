package com.company.classes;

import java.util.ArrayList;

public class Olympiad
{
    public String shortname;
    public String name;
    public String info;
    public ArrayList<String> links;

    public ArrayList<Subject> subjects;
    public String field;

    public ArrayList<OlympEvent> events;

    public Olympiad()
    {
        shortname = "";
        name = "";
        info = "";
        links = new ArrayList<String>();
        subjects = new ArrayList<Subject>();
        field = "";
        events = new ArrayList<OlympEvent>();
    }

    public String toString()
    {
        return shortname;
    }
    public boolean show;
}
