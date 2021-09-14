package com.kriticalflare.community.di;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class PrefModule {
    @Singleton
    @Provides
    public RxDataStore<Preferences> provideDataStore(@ApplicationContext Context context) {
        return new RxPreferenceDataStoreBuilder(context, "prefs").build();
    }
}
