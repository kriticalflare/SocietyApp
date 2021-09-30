package com.kriticalflare.community.parking.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ParkingResponse{

	@SerializedName("isParked")
	private boolean isParked;

	@SerializedName("message")
	private String message;

	public boolean isParked(){
		return isParked;
	}

	public String getMessage(){
		return message;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"ParkingResponse{" + 
			"isParked = '" + isParked + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}