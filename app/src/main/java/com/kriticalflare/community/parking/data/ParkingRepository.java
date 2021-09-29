package com.kriticalflare.community.parking.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kriticalflare.community.network.AuthenticatedService;
import com.kriticalflare.community.parking.data.model.GetParkingResponse;
import com.kriticalflare.community.util.AppExecutor;
import com.kriticalflare.community.util.Resource;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingRepository {
    AppExecutor appExecutor;
    AuthenticatedService authService;

    @Inject
    ParkingRepository(AppExecutor appExecutor, AuthenticatedService authService) {
        this.appExecutor = appExecutor;
        this.authService = authService;
    }

    public LiveData<Resource<GetParkingResponse>> getParkingSpots() {
        MutableLiveData<Resource<GetParkingResponse>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading(null));
        appExecutor.networkIO().execute(() -> {
            authService.getParkingSpots().enqueue(new Callback<GetParkingResponse>() {
                @Override
                public void onResponse(Call<GetParkingResponse> call, Response<GetParkingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        liveData.postValue(Resource.success(response.body()));
                    } else {
                        liveData.postValue(Resource.error(null, response.message()));
                    }
                }

                @Override
                public void onFailure(Call<GetParkingResponse> call, Throwable t) {
                    liveData.postValue(Resource.error(null, t.getMessage()));
                }
            });
        });
        return liveData;
    }
}
