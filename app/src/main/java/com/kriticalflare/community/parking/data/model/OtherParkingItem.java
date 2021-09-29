package com.kriticalflare.community.parking.data.model;

import com.google.gson.annotations.SerializedName;

public class OtherParkingItem{

	@SerializedName("User")
	private String user;

	@SerializedName("qrCode")
	private String qrCode;

	@SerializedName("Long")
	private String longitude;

	@SerializedName("_id")
	private String id;

	@SerializedName("Lat")
	private String latitude;

	public String getUser(){
		return user;
	}

	public String getQrCode(){
		return qrCode;
	}

	public String getLongitude(){
		return longitude;
	}

	public String getId(){
		return id;
	}

	public String getLatitude(){
		return latitude;
	}
}