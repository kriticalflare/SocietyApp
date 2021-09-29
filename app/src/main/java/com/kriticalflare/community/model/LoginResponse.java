package com.kriticalflare.community.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class LoginResponse{

	@SerializedName("password")
	private String password;

	@SerializedName("__v")
	private int V;

	@SerializedName("name")
	private String name;

	@SerializedName("room_number")
	private String roomNumber;

	@SerializedName("_id")
	private String id;

	@SerializedName("building_number")
	private String buildingNumber;

	@SerializedName("email")
	private String email;

	@SerializedName("token")
	private String token;

	public String getPassword(){
		return password;
	}

	public int getV(){
		return V;
	}

	public String getName(){
		return name;
	}

	public String getRoomNumber(){
		return roomNumber;
	}

	public String getId(){
		return id;
	}

	public String getBuildingNumber(){
		return buildingNumber;
	}

	public String getEmail(){
		return email;
	}

	@NonNull
	public String getToken(){
		return token;
	}
}