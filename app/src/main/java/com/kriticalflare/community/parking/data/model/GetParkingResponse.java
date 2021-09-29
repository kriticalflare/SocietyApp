package com.kriticalflare.community.parking.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetParkingResponse{

	@SerializedName("userParking")
	private List<UserParkingItem> userParking;

	@SerializedName("otherParking")
	private List<OtherParkingItem> otherParking;

	public List<UserParkingItem> getUserParking(){
		return userParking;
	}

	public List<OtherParkingItem> getOtherParking(){
		return otherParking;
	}
}