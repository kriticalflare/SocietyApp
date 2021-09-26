package com.kriticalflare.community.events.data.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventsDao {

    @Query("SELECT * FROM events")
    List<Events> getEvents();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvents(List<Events> eventsList);
}
