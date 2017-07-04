package com.company.classes;

import com.company.classes.Faculty;

import java.util.ArrayList;

public class University
{
    public int ID;
    public String name;
    public String shortname;
    public String about;
    public ArrayList<Faculty> faculties;
    public ArrayList<String> links;

    public University()
    {
        ID = -1;
        name = "";
        shortname = "";
        about = "";
        faculties = new ArrayList<Faculty>();
        links = new ArrayList<String>();
    }
}
