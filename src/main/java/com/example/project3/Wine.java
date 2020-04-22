package com.example.project3;

import java.io.Serializable;

public class Wine implements Serializable
{
    private int id;
    private String name;
    private int grapeid;
    private int q;
    private String time;

    public Wine(int id, String name, int grapeid, int q, String time)
    {
        this.id = id;
        this.name = name;
        this.grapeid = grapeid;
        this.q = q;
        this.time = time;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrape()
    {
        return grapeid;
    }

    public void setGrape(int grapeid)
    {
        this.grapeid = grapeid;
    }

    public int getQ()
    {
        return q;
    }

    public void setQ(int q)
    {
        this.q = q;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
