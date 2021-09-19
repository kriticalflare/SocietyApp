package com.kriticalflare.community.emergency.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "emergencies")
public class EmergencyItem{

	@SerializedName("number")
	private String number;

	@SerializedName("name")
	private String name;

	@PrimaryKey
	@NonNull
	@SerializedName("_id")
	private String id;

	@SerializedName("Job_title")
	private String jobTitle;

	public void setNumber(String number){
		this.number = number;
	}

	public String getNumber(){
		return number;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setJobTitle(String jobTitle){
		this.jobTitle = jobTitle;
	}

	public String getJobTitle(){
		return jobTitle;
	}

	public EmergencyItem(String number, String name, @NonNull String id, String jobTitle) {
		this.number = number;
		this.name = name;
		this.id = id;
		this.jobTitle = jobTitle;
	}

	@Override
 	public String toString(){
		return 
			"EmergencyNumberListItem{" + 
			"number = '" + number + '\'' + 
			",name = '" + name + '\'' + 
			",_id = '" + id + '\'' + 
			",job_title = '" + jobTitle + '\'' + 
			"}";
		}
}