package com.kriticalflare.community.network;

import com.kriticalflare.community.emergency.data.model.EmergencyResponse;
import com.kriticalflare.community.meetings.data.model.MeetingsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("meeting")
    Call<MeetingsResponse> getMeetings();

    @GET("emergency_number")
    Call<EmergencyResponse> getEmergencies();
}
