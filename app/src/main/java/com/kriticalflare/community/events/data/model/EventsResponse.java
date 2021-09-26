package com.kriticalflare.community.events.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class EventsResponse{

	@SerializedName("eventList")
	private List<Events> eventList;

	public void setEventList(List<Events> eventList){
		this.eventList = eventList;
	}

	public List<Events> getEventList(){
		return eventList;
	}

	@Override
 	public String toString(){
		return 
			"EventsResponse{" + 
			"eventList = '" + eventList + '\'' + 
			"}";
		}
}