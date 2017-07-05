package com.company.classes;

import java.util.ArrayList;

public class Faculty
{
    public University university;

    public String name;
    public String shortname;
    public String info;
    public ArrayList<String> links;

    public ArrayList<EduProgram> eduprograms;

    public Faculty()
    {
        university = null;
        name = "";
        shortname = "";
        info = "";
        eduprograms = new ArrayList<EduProgram>();
        links = new ArrayList<String>();
    }
}
