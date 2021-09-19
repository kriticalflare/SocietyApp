package com.kriticalflare.community.emergency.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class EmergencyResponse {

	@SerializedName("emergencyNumberList")
	private List<EmergencyItem> emergencyNumberList;

	public void setEmergencyNumberList(List<EmergencyItem> emergencyNumberList){
		this.emergencyNumberList = emergencyNumberList;
	}

	public List<EmergencyItem> getEmergencyNumberList(){
		return emergencyNumberList;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"emergencyNumberList = '" + emergencyNumberList + '\'' + 
			"}";
		}
}