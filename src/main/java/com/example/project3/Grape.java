package com.example.project3;

import java.io.Serializable;

public class Grape implements Serializable
{
    private int id;
    private String name;
    private int type;
    private String producer;
    private int q;
    private String time;

    public Grape(int id, String name, int type, String producer, int q, String time)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.producer = producer;
        this.q = q;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }
}
