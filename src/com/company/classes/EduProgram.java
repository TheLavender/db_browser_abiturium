package com.company.classes;

import java.util.ArrayList;

public class EduProgram
{
    public Faculty faculty;

    public String name;
    public String shortname;
    public String info;
    public ArrayList <String> links;
    public ArrayList <Olympiad> olympiads;

    public EduProgram()
    {
        faculty = null;
        name = "";
        shortname = "";
        info = "";
        links = new ArrayList<String>();
        olympiads = new ArrayList<Olympiad>();
    }
}