package com.company.classes;

import java.util.ArrayList;

public class University
{
    public String name;
    public String shortname;
    public String info;
    public ArrayList<Faculty> faculties;
    public ArrayList<String> links;

    public University()
    {
        name = "";
        shortname = "";
        info = "";
        faculties = new ArrayList<Faculty>();
        links = new ArrayList<String>();
    }
}
