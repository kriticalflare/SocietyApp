package com.kriticalflare.community.emergency.data;

import androidx.lifecycle.LiveData;

import com.kriticalflare.community.emergency.data.model.EmergencyDao;
import com.kriticalflare.community.emergency.data.model.EmergencyItem;
import com.kriticalflare.community.emergency.data.model.EmergencyResponse;
import com.kriticalflare.community.network.ApiService;
import com.kriticalflare.community.util.AppExecutor;
import com.kriticalflare.community.util.NetworkBoundResource;
import com.kriticalflare.community.util.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;

public class EmergencyRepository {
    private AppExecutor appExecutor;
    private EmergencyDao emergencyDao;
    private NetworkBoundResource<List<EmergencyItem>, EmergencyResponse> networkBoundResource;
    private ApiService apiService;

    @Inject
    public EmergencyRepository(AppExecutor appExecutor, EmergencyDao emergencyDao, ApiService apiService) {
        this.appExecutor = appExecutor;
        this.emergencyDao = emergencyDao;
        this.apiService = apiService;
    }

    public LiveData<Resource<List<EmergencyItem>>> getEmergencies() {
        networkBoundResource = new NetworkBoundResource<List<EmergencyItem>, EmergencyResponse>(appExecutor) {
            @Override
            public List<EmergencyItem> loadFromDatabase() {
                return emergencyDao.getEmergencies();
            }

            @Override
            public Call<EmergencyResponse> createApiCall() {
                return apiService.getEmergencies();
            }

            @Override
            public void saveCallResult(EmergencyResponse item) {
                emergencyDao.insertEmergency(item.getEmergencyNumberList());
            }

            @Override
            public boolean isSuccessfulResponse(Response<EmergencyResponse> item) {
                return item.isSuccessful();
            }
        };
        return networkBoundResource.asLiveData();
    }
}
