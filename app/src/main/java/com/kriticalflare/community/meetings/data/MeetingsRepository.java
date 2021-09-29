package com.kriticalflare.community.meetings.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kriticalflare.community.meetings.data.model.Meeting;
import com.kriticalflare.community.meetings.data.model.MeetingDao;
import com.kriticalflare.community.meetings.data.model.MeetingsResponse;
import com.kriticalflare.community.network.ApiService;
import com.kriticalflare.community.util.AppExecutor;
import com.kriticalflare.community.util.NetworkBoundResource;
import com.kriticalflare.community.util.Resource;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

public class MeetingsRepository {
    private AppExecutor appExecutor;
    private MeetingDao meetingDao;
    private NetworkBoundResource<List<Meeting>, MeetingsResponse> networkBoundResource;
    private ApiService apiService;

    @Inject
    public MeetingsRepository(AppExecutor appExecutor, MeetingDao meetingDao, ApiService apiService) {
        this.appExecutor = appExecutor;
        this.meetingDao = meetingDao;
        this.apiService = apiService;
    }

    public LiveData<Resource<List<Meeting>>> getMeetings(){
        networkBoundResource = new NetworkBoundResource<List<Meeting>, MeetingsResponse>(appExecutor) {
            @Override
            public List<Meeting> loadFromDatabase() {
                return meetingDao.getMeetings();
            }

            @Override
            public Call<MeetingsResponse> createApiCall() {
                return apiService.getMeetings();
            }

            @Override
            public void saveCallResult(MeetingsResponse item) {
                meetingDao.insertMeetings(item.getMeetingsList());
            }

            @Override
            public boolean isSuccessfulResponse(Response<MeetingsResponse> item) {
                return item.isSuccessful();
            }
        };
        return networkBoundResource.asLiveData();
    }

    public void refreshMeetings(){
        networkBoundResource.triggerNetworkRequest();
    }
}
