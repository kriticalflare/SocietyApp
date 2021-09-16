package com.kriticalflare.community.meetings.data.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MeetingDao {
    @Query("SELECT * FROM meetings")
    List<Meeting> getMeetings();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeetings(List<Meeting> meetingList);
}
