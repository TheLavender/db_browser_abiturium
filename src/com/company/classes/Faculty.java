package com.company.classes;

import com.company.classes.EduProgram;

import java.util.ArrayList;

public class Faculty
{
    public String name;
    public String shortname;
    public String about;
    public ArrayList<String> links;

    public ArrayList<EduProgram> eduprograms;

    public Faculty()
    {
        name = "";
        shortname = "";
        about = "";
        eduprograms = new ArrayList<EduProgram>();
        links = new ArrayList<String>();
    }
}
