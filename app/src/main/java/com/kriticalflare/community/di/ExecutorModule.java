package com.kriticalflare.community.di;

import com.kriticalflare.community.util.AppExecutor;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ExecutorModule {
    @Provides
    AppExecutor providesAppExecutor() {
        return new AppExecutor();
    }
}
