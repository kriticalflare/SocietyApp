package com.kriticalflare.community.network;

import androidx.annotation.NonNull;

import com.kriticalflare.community.data.AuthenticationRepository;
import com.kriticalflare.community.data.local.PrefsDataStore;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private PrefsDataStore dataStore;

    @Inject
    public TokenInterceptor(PrefsDataStore dataStore){
        this.dataStore = dataStore;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = dataStore.getJWTToken().blockingFirst();
        Request.Builder newRequest = originalRequest
                .newBuilder()
                .addHeader("x-access-token", token);
        return chain.proceed(newRequest.build());
    }
}
