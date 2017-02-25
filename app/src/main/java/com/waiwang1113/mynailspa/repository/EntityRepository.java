package com.waiwang1113.mynailspa.repository;

import android.support.annotation.NonNull;

import com.waiwang1113.mynailspa.entities.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Generic repository interface for handling
 * Created by Weige on 2/16/17.
 */

public interface EntityRepository {

    public void add(Entity newValue);
    public List<Entity> getAll(Class<? extends Entity> clazz);
    public void remove(Entity value);
    public void update(Entity value);

}
