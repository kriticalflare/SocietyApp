package com.kriticalflare.community.emergency.data.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmergencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertEmergency(List<EmergencyItem> emergencyItemList);

    @Query("SELECT * FROM emergencies")
    public List<EmergencyItem> getEmergencies();
}
