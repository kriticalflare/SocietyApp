package com.kriticalflare.community.network;

import com.kriticalflare.community.parking.data.model.GetParkingResponse;
import com.kriticalflare.community.parking.data.model.ParkingBody;
import com.kriticalflare.community.parking.data.model.ParkingResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

public interface AuthenticatedService {

    @GET("parking")
    Call<GetParkingResponse> getParkingSpots();

    @POST("parking")
    Call<ParkingResponse> claimParkingSpot(@Body ParkingBody parkingBody);

    @HTTP(method = "DELETE", path = "parking", hasBody = true)
    Call<ParkingResponse> clearParkingSpot(@Body ParkingBody parkingBody);
}
