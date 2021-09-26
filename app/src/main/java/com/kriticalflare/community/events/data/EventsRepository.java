package com.kriticalflare.community.events.data;

import androidx.lifecycle.LiveData;

import com.kriticalflare.community.emergency.data.model.EmergencyDao;
import com.kriticalflare.community.emergency.data.model.EmergencyItem;
import com.kriticalflare.community.emergency.data.model.EmergencyResponse;
import com.kriticalflare.community.events.data.model.Events;
import com.kriticalflare.community.events.data.model.EventsDao;
import com.kriticalflare.community.events.data.model.EventsResponse;
import com.kriticalflare.community.network.ApiService;
import com.kriticalflare.community.util.AppExecutor;
import com.kriticalflare.community.util.NetworkBoundResource;
import com.kriticalflare.community.util.Resource;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

public class EventsRepository {
    private AppExecutor appExecutor;
    private  EventsDao eventsDao;
    private NetworkBoundResource<List<Events>, EventsResponse> networkBoundResource;
    private ApiService apiService;

    @Inject
    public EventsRepository(AppExecutor appExecutor, EventsDao eventsDao, ApiService apiService) {
        this.appExecutor = appExecutor;
        this.eventsDao = eventsDao;
        this.apiService = apiService;
    }

    public LiveData<Resource<List<Events>>> getEvents() {
        networkBoundResource = new NetworkBoundResource<List<Events>, EventsResponse>(appExecutor) {
            @Override
            public List<Events> loadFromDatabase() {
                return eventsDao.getEvents();
            }

            @Override
            public Call<EventsResponse> createApiCall() {
                return apiService.getEvents();
            }

            @Override
            public void saveCallResult(EventsResponse item) {
                eventsDao.insertEvents(item.getEventList());
            }

            @Override
            public boolean isSuccessfulResponse(Response<EventsResponse> item) {
                return item.isSuccessful();
            }
        };
        return networkBoundResource.asLiveData();
    }
}
