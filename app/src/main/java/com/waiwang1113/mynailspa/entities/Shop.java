package com.waiwang1113.mynailspa.entities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weige on 2/12/17.
 */

public class Shop implements Entity{
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String city;
    private String state;
    private String zipCode;
    private List<Service> services;
    public Shop() {
        services = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", services=" + services +
                '}';
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getDetail(){
        return String.format("%s %s %s",getCity(),getState(),getFormattedPhoneNumber());
    }

    public String getFormattedPhoneNumber() {
        return String.format("(%s)%s-%s",phoneNumber.substring(0,3)
                ,phoneNumber.substring(3,6)
                ,phoneNumber.substring(6));
    }
    public void addService(@NotNull Service s){
        this.services.add(s);
    }
}
