package com.waiwang1113.mynailspa.request;

import com.waiwang1113.mynailspa.entities.Appointment;
import com.waiwang1113.mynailspa.entities.Entity;
import com.waiwang1113.mynailspa.entities.Service;
import com.waiwang1113.mynailspa.entities.Shop;
import com.waiwang1113.mynailspa.response.OnResponseBack;
import com.waiwang1113.mynailspa.response.Response;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Weige on 2/19/17.
 */

public class DummyRequestDispatcher implements RequestDispatcher{
    Map<Class<? extends Entity>,Set<Entity>> buffer;
    Map<Integer,Set<Appointment>> mShopAppointments;
    public DummyRequestDispatcher() {
        buffer = new HashMap<>();
        mShopAppointments=new HashMap<>();
        Shop newShop=new Shop();
        newShop.setName("Luxe Nail Spa");
        newShop.setCity("Westport");
        newShop.setState("NY");
        newShop.setId(12345);
        newShop.setPhoneNumber("3472955818");
        buffer.put(Shop.class, new HashSet<>(Arrays.asList((Entity)newShop)));
        Service Manicure = new Service();
        Manicure.setLength(30);
        Manicure.setName("Manicure");
        newShop.addService(Manicure);
        Service Pedicure = new Service();
        Pedicure.setLength(30);
        Pedicure.setName("Pedicure");
        newShop.addService(Pedicure);
        Service Waxing = new Service();
        Waxing.setLength(60);
        Waxing.setName("Waxing");
        newShop.addService(Waxing);
        Set<Appointment> appointments=new HashSet<>();
        mShopAppointments.put(12345,appointments);
        long startTime=new Date().getTime();
        appointments.add(newAppointment(12345,new Date(startTime+ TimeUnit.MINUTES.toMillis(1440)),Pedicure));
        appointments.add(newAppointment(12345,new Date(startTime+ TimeUnit.MINUTES.toMillis(4000)),Pedicure,Manicure));
    }
    private Appointment newAppointment(int shopId,Date time,Service ...service){
        Appointment app=new Appointment();
        app.setShopID(shopId);
        app.setTime(time);
        for(Service s:service){
            app.addService(s);
        }
        return app;
    }
    @Override
    public Response<Shop> getShopList() {
        List<Shop> list = new ArrayList<>();
        for(Entity s:buffer.get(Shop.class))list.add((Shop)s);
        return Response.getPayloadResponse(list);

    }

    @Override
    public Response<Shop> getShop(int id) {
        if(id==12345){
            List<Shop> list = new ArrayList<>();
            for(Entity s:buffer.get(Shop.class))list.add((Shop)s);
            return Response.getPayloadResponse(list);
        }else{
            return Response.getEmptyResponse(Response.ResponseCode.FAIL,"No shop found for the given id");
        }
    }

    @Override
    public Response<Appointment> getAllAppointment(@NotNull int shopId) {
        return Response.getPayloadResponse(new ArrayList<Appointment>(mShopAppointments.get(shopId)));
    }

    @Override
    public Response<Appointment> updateAppointment(@NotNull Appointment appointment) {
        return null;
    }

    @Override
    public Response<Appointment> removeAppointment(@NotNull Appointment appointment) {
        return null;
    }

    @Override
    public Response<Appointment> addAppointment(@NotNull int shopId, @NotNull Appointment appointment) {
        if(mShopAppointments.get(shopId)==null){
            mShopAppointments.put(shopId,new HashSet<Appointment>());
        }
        mShopAppointments.get(shopId).add(appointment);
        return null;
    }

    private boolean removeAppointmentHelp(Appointment appointment){
        Set<Entity> set=buffer.get(Appointment.class);
        if(set==null || !set.contains(appointment)){
            return false;
        }else{
            set.remove(appointment);
            return true;
        }
    }
}
