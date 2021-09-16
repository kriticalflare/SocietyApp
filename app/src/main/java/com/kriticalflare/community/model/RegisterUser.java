package com.kriticalflare.community.model;
/*
{
    "name": "krithik",
    "room_number": "10",
    "buil_number": "e3",
    "password": "12345689",
    "email": "krithiksuvarna@gmail.com"
}
 */

import com.google.gson.annotations.SerializedName;

public class RegisterUser {
    @SerializedName("name")
    private String name;

    @SerializedName("room_number")
    private String roomNumber;

    @SerializedName("building_number")
    private String buildingNumber;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    public RegisterUser(String name, String email, String password, String roomNumber, String buildingNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roomNumber = roomNumber;
        this.buildingNumber = buildingNumber;
    }
}
