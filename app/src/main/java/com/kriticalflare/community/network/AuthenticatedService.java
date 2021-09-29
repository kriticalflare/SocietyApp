package com.kriticalflare.community.network;

import com.kriticalflare.community.parking.data.model.GetParkingResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AuthenticatedService {

    @GET("parking")
    Call<GetParkingResponse> getParkingSpots();
}
