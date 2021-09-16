package com.kriticalflare.community.di;

import com.kriticalflare.community.network.ApiService;
import com.kriticalflare.community.network.AuthService;
import com.kriticalflare.community.util.AppExecutor;
import com.kriticalflare.community.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    }

    @Singleton
    @Provides
    public AuthService provideAuthClientApi(OkHttpClient okHttpClient, AppExecutor appExecutor) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(appExecutor.networkIO())
                .build()
                .create(AuthService.class);
    }

    @Singleton
    @Provides
    public ApiService provideApiClient(OkHttpClient okHttpClient, AppExecutor appExecutor) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(appExecutor.networkIO())
                .build()
                .create(ApiService.class);
    }
}
