package com.kriticalflare.community.data;

import androidx.annotation.NonNull;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kriticalflare.community.data.local.PrefsDataStore;
import com.kriticalflare.community.model.LoginResponse;
import com.kriticalflare.community.model.LoginUser;
import com.kriticalflare.community.model.RegisterUser;
import com.kriticalflare.community.network.AuthService;
import com.kriticalflare.community.util.AppExecutor;
import com.zhuinden.eventemitter.EventEmitter;
import com.zhuinden.eventemitter.EventSource;

import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthenticationRepository {

    private PrefsDataStore dataStore;
    private final AuthService authService;
    private final EventEmitter<String> emitter;
    public EventSource<String> errorEvents;
    private final MutableLiveData<Boolean> _loadingLiveData;
    public LiveData<Boolean> loading;
    private AppExecutor appExecutor;

    @Inject
    AuthenticationRepository(PrefsDataStore dataStore, AuthService authService, AppExecutor appExecutor) {
        this.dataStore = dataStore;
        this.authService = authService;
        this.appExecutor = appExecutor;
        emitter = new EventEmitter<>();
        _loadingLiveData = new MutableLiveData<>(false);
        loading = _loadingLiveData;
        errorEvents = emitter;
    }

    public void register(RegisterUser user) {
        _loadingLiveData.setValue(true);
        authService.registerUser(user).enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                if (response.isSuccessful()) {
                    _loadingLiveData.postValue(false);
                    appExecutor.mainThread().execute(() -> {
                        emitter.emit("Registration Successful");
                    });

                } else {
                    _loadingLiveData.postValue(false);
                    appExecutor.mainThread().execute(() -> {
                        emitter.emit(response.message());
                    });
                }
            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                _loadingLiveData.postValue(false);
                appExecutor.mainThread().execute(() -> {
                    emitter.emit("Registration failed");
                });
            }
        });
    }

    public void login(LoginUser user) {
        _loadingLiveData.setValue(true);
        authService.login(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    _loadingLiveData.postValue(false);
                    if(response.body() != null){
                        dataStore.saveEmail(user.email);
                        dataStore.saveToken(response.body().getToken());
                        dataStore.saveLoginStatus(true);
                    } else {
                        dataStore.saveLoginStatus(false);
                    }
                } else {
                    _loadingLiveData.postValue(false);
                    appExecutor.mainThread().execute(() -> {
                        emitter.emit("Check your credentials");
                    });

                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                _loadingLiveData.postValue(false);
                appExecutor.mainThread().execute(() -> {
                    emitter.emit("Login failed");
                });
                dataStore.saveLoginStatus(false);
            }
        });
    }

    public void logout() {
        dataStore.saveEmail("");
        dataStore.saveLoginStatus(false);
        dataStore.saveToken("");
    }

    public void saveLoginStatus(boolean status){
        dataStore.saveLoginStatus(status);
    }

    public Flowable<Boolean> getLoginStatus() {
        return dataStore.getLoginStatus();
    }

    public Flowable<String> getJWTToken() {
        return dataStore.getJWTToken();
    }
}
