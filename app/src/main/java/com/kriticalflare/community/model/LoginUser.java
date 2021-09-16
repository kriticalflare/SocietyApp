package com.kriticalflare.community.model;

import com.google.gson.annotations.SerializedName;

public class LoginUser {
    @SerializedName("password")
    private String password;

    @SerializedName("email")
    public String email;

    public LoginUser(String email,String password) {
        this.email = email;
        this.password = password;
    }
}
