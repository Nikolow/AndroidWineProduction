package com.example.project3;

import java.io.Serializable;

public class Bottles implements Serializable
{
    private int id;
    private String name;
    private int type;
    private int ml;
    private String time;

    public Bottles(int id, String name, int type, int ml, String time)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ml = ml;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getMl()
    {
        return ml;
    }

    public void setMl(int ml)
    {
        this.ml = ml;
    }
}
