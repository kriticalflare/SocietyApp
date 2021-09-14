package com.kriticalflare.community.network;

import com.kriticalflare.community.model.LoginUser;
import com.kriticalflare.community.model.RegisterUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("register")
    Call<RegisterUser> registerUser(@Body RegisterUser user);

    @POST("login")
    Call<LoginUser> login(@Body LoginUser user);
}