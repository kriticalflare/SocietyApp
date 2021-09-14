package com.kriticalflare.community.data;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;

import com.kriticalflare.community.model.LoginUser;
import com.kriticalflare.community.model.RegisterUser;
import com.kriticalflare.community.network.AuthService;
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

    private final RxDataStore<Preferences> dataStore;
    private final AuthService authService;
    private final EventEmitter<String> emitter;
    public EventSource<String> errorEvents;

    @Inject
    AuthenticationRepository(RxDataStore<Preferences> dataStore, AuthService authService) {
        this.dataStore = dataStore;
        this.authService = authService;
        emitter = new EventEmitter<>();
        errorEvents = emitter;
    }

    public void saveEmail(String email) {
        dataStore.updateDataAsync(preferences -> {
            MutablePreferences _prefs = preferences.toMutablePreferences();
            _prefs.set(KEY_EMAIL, email);
            return Single.just(_prefs);
        });
    }

    public void saveLoginStatus(boolean status) {
        dataStore.updateDataAsync(preferences -> {
            MutablePreferences _prefs = preferences.toMutablePreferences();
            _prefs.set(KEY_IS_LOGGED_IN, status);
            return Single.just(_prefs);
        });
    }

    public void register(RegisterUser user){
        authService.registerUser(user).enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                if(response.isSuccessful()){
                    emitter.emit("Registration Successful");
                }
            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                emitter.emit("Registration failed");
            }
        });
    }

    public void login(LoginUser user){
        authService.login(user).enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                if(response.isSuccessful()){
                    saveEmail(user.email);
                    saveLoginStatus(true);
                }
            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {
                emitter.emit("Login failed");
            }
        });
    }

    public Flowable<Boolean> getLoginStatus() {
        return dataStore.data().map(preferences -> Optional.ofNullable(preferences.get(KEY_IS_LOGGED_IN)).orElse(false));
    }

    public static final Preferences.Key<String> KEY_EMAIL = PreferencesKeys.stringKey("key_email");
    public static final Preferences.Key<Boolean> KEY_IS_LOGGED_IN =
            PreferencesKeys.booleanKey("key_is_logged_in");
    public static final Preferences.Key<String> KEY_JWT = PreferencesKeys.stringKey("jwt");
}
