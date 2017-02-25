package com.waiwang1113.mynailspa.entities;

/**
 * Created by Weige on 2/15/17.
 */

public class Service implements Entity{
    private String name;
    private int length;
    public Service(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override public String toString(){return this.name;}

}
