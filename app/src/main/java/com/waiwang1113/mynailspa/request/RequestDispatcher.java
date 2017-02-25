package com.waiwang1113.mynailspa.request;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.waiwang1113.mynailspa.entities.Appointment;
import com.waiwang1113.mynailspa.entities.Entity;
import com.waiwang1113.mynailspa.entities.Shop;
import com.waiwang1113.mynailspa.response.OnResponseBack;
import com.waiwang1113.mynailspa.response.Response;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Interfaces for sending request to the server and handling the response
 * Created by Weige on 2/19/17.
 */

public interface RequestDispatcher {
    
    Response<Shop> getShopList();
    
    Response<Shop> getShop(int id);
    
    Response<Appointment> getAllAppointment(@NotNull int shopId);
    
    Response<Appointment> updateAppointment(@NotNull Appointment appointment);
    
    Response<Appointment> removeAppointment(@NotNull Appointment appointment);
     
    Response<Appointment> addAppointment(@NotNull int shopId,@NotNull Appointment appointment);
}
