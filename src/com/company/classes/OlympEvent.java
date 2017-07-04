package com.company.classes;



import com.company.classes.Category;

import java.util.ArrayList;
import java.util.Calendar;


public class OlympEvent
{
    public Category cat;
    public String name;
    public String info;
    public ArrayList <String> links;

    public boolean immediate = false;
    public Calendar begin;
    public Calendar end;

    public boolean hidden = false;
    public boolean done = false;

    public OlympEvent()
    {
        cat = null;
        name = "";
        info = "";
        links = new ArrayList<String>();
        begin = null;
        end = null;
    }

    public String toString()
    {
        return name;
    }
}