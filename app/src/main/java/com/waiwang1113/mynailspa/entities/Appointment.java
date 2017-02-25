package com.waiwang1113.mynailspa.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Weige on 2/15/17.
 */

public class Appointment implements Entity{
    private int shopID;
    private Date time;
    private List<Service> services;
    private int id;

    public Appointment() {
        services=new ArrayList<Service>();
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
    public void addService(Service s){
        this.services.add(s);
    }
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "shop=" + shopID +
                ", time=" + time +
                ", service=" + getServices()+
                '}';
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
