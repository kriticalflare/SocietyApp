package com.kriticalflare.community.di;

import android.content.Context;

import androidx.room.Room;

import com.kriticalflare.community.data.local.AppDatabase;
import com.kriticalflare.community.meetings.data.model.MeetingDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Singleton
    @Provides
    AppDatabase providesAppDatabase(@ApplicationContext Context context) {
        return  Room
                .databaseBuilder(context, AppDatabase.class, "SocietyDatabase")
                .fallbackToDestructiveMigration().build();
    }

    @Provides
    MeetingDao providesMeetingDao(AppDatabase database){
        return database.meetingDao();
    }
}
