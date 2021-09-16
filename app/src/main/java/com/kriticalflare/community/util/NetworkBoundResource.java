package com.kriticalflare.community.util;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class NetworkBoundResource<ResultType, RequestType> {

    private MutableLiveData<Resource<ResultType>> result = new MutableLiveData<>();

    private AppExecutor appExecutor;


    public NetworkBoundResource(AppExecutor appExecutor) {
        this.appExecutor = appExecutor;
    }

    @WorkerThread
    abstract public ResultType loadFromDatabase();

    @WorkerThread
    abstract public Call<RequestType> createApiCall();

    @WorkerThread
    abstract public void saveCallResult(RequestType item);

    abstract public boolean isSuccessfulResponse(Response<RequestType> item);

    public void triggerNetworkRequest() {
        appExecutor.networkIO().execute(this::loadFromNetwork);
    }

    private void loadFromNetwork() {

        result.postValue(Resource.loading(loadFromDatabase()));

        createApiCall().enqueue(new Callback<RequestType>() {
            @Override
            public void onResponse(Call<RequestType> call, Response<RequestType> response) {
                if (response.body() == null || String.valueOf(response.code()).matches("^[45].*")) {
                    result.postValue(Resource.error(loadFromDatabase(),
                            "Server is currently experiencing issues, try again after some time"));
                } else if (isSuccessfulResponse(response)) {
                    saveCallResult(response.body());
                    result.postValue(Resource.success(loadFromDatabase()));
                } else {
                    result.postValue(Resource.error(loadFromDatabase(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<RequestType> call, Throwable t) {
                result.postValue(Resource.error(loadFromDatabase(),
                        "Oops! Something went wrong"));
            }
        });
    }

    public MutableLiveData<Resource<ResultType>> asLiveData() {
        triggerNetworkRequest();
        return result;
    }

}