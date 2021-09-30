package com.kriticalflare.community.parking.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kriticalflare.community.network.AuthenticatedService;
import com.kriticalflare.community.parking.data.model.GetParkingResponse;
import com.kriticalflare.community.parking.data.model.ParkingBody;
import com.kriticalflare.community.parking.data.model.ParkingResponse;
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

    public LiveData<Resource<ParkingResponse>> claimParkingSpots(String qrCode) {
        MutableLiveData<Resource<ParkingResponse>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading(null));
        appExecutor.networkIO().execute(() -> {
            ParkingBody parkingBody = new ParkingBody(qrCode);
            authService.claimParkingSpot(parkingBody).enqueue(new Callback<ParkingResponse>() {
                @Override
                public void onResponse(Call<ParkingResponse> call, Response<ParkingResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isParked()) {
                        liveData.postValue(Resource.success(response.body()));
                    } else {
                        if (response.body() != null) {
                            liveData.postValue(Resource.error(response.body(), response.message()));
                        } else {
                            liveData.postValue(Resource.error(null, "Internal Server Error"));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ParkingResponse> call, Throwable t) {
                    liveData.postValue(Resource.error(null, t.getMessage()));
                }
            });
        });

        return liveData;
    }

    public LiveData<Resource<ParkingResponse>> clearParkingSpot(String qrCode) {
        MutableLiveData<Resource<ParkingResponse>> liveData = new MutableLiveData<>();
        liveData.postValue(Resource.loading(null));
        appExecutor.networkIO().execute(() -> {
            ParkingBody parkingBody = new ParkingBody(qrCode);
            authService.clearParkingSpot(parkingBody).enqueue(new Callback<ParkingResponse>() {
                @Override
                public void onResponse(Call<ParkingResponse> call, Response<ParkingResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isParked()) {
                        liveData.postValue(Resource.success(response.body()));
                    } else {
                        if (response.body() != null) {
                            liveData.postValue(Resource.error(response.body(), response.message()));
                        } else {
                            liveData.postValue(Resource.error(null, "Internal Server Error"));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ParkingResponse> call, Throwable t) {
                    liveData.postValue(Resource.error(null, t.getMessage()));
                }
            });
        });

        return liveData;
    }
}
