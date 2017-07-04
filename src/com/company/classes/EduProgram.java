package com.company.classes;

import java.util.ArrayList;

public class EduProgram
{
    public String name;
    public String shortname;
    public String about;
    public ArrayList <String> links;
    public ArrayList <Olympiad> olympiads;

    public EduProgram()
    {
        name = "";
        shortname = "";
        about = "";
        links = new ArrayList<String>();
        olympiads = new ArrayList<Olympiad>();
    }
}