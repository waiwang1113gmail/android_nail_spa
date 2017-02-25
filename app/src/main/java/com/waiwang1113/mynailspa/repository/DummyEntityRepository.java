package com.waiwang1113.mynailspa.repository;

import com.waiwang1113.mynailspa.entities.Entity;
import com.waiwang1113.mynailspa.entities.Service;
import com.waiwang1113.mynailspa.entities.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dummy EntityRepository for development and testing
 * Created by Weige on 2/17/17.
 */

public class DummyEntityRepository implements EntityRepository{
    Map<Class<? extends Entity>,List<Entity>> buffer;
    public DummyEntityRepository() {
        buffer = new HashMap<>();
        Shop newShop=new Shop();
        newShop.setName("Luxe Nail Spa");
        newShop.setCity("Westport");
        newShop.setState("NY");
        newShop.setPhoneNumber("3472955818");
        add(newShop);
        Service service = new Service();
        service.setLength(30);
        service.setName("Manicure");
        newShop.addService(service);
        service = new Service();
        service.setLength(30);
        service.setName("Pedicure");
        newShop.addService(service);
        service = new Service();
        service.setLength(60);
        service.setName("Waxing");
        newShop.addService(service);

    }




    @Override
    public void add(Entity newValue) {
        if(!buffer.containsKey(newValue.getClass())){
            buffer.put(newValue.getClass(),new ArrayList<Entity>());
        }
        buffer.get(newValue.getClass()).add(newValue);
    }

    @Override
    public List<Entity> getAll(Class<? extends Entity> clazz) {
        return buffer.get(clazz);
    }

    @Override
    public void remove(Entity value) {
        buffer.get(value.getClass()).remove(value);
    }

    @Override
    public void update(Entity value) {

    }
}
