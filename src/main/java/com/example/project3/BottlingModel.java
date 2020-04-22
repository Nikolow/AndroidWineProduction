package com.example.project3;

import java.io.Serializable;

public class BottlingModel implements Serializable
{
    private int id;
    private String name;
    private int wineid;
    private int bottleid;
    private String time;

    public BottlingModel(int id, String name, int wineid, int bottleid, String time)
    {
        this.name = name;
        this.id = id;
        this.wineid = wineid;
        this.bottleid = bottleid;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWineid() {
        return wineid;
    }

    public void setWineid(int wineid) {
        this.wineid = wineid;
    }

    public int getBottleid() {
        return bottleid;
    }

    public void setBottleid(int bottleid) {
        this.bottleid = bottleid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
