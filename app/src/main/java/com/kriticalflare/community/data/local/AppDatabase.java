package com.kriticalflare.community.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.kriticalflare.community.emergency.data.model.EmergencyDao;
import com.kriticalflare.community.emergency.data.model.EmergencyItem;
import com.kriticalflare.community.meetings.data.model.Meeting;
import com.kriticalflare.community.meetings.data.model.MeetingDao;

@Database(entities = {Meeting.class, EmergencyItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MeetingDao meetingDao();
    public abstract EmergencyDao emergencyDao();
}
