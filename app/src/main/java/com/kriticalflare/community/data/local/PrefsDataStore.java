package com.kriticalflare.community.data.local;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;

import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class PrefsDataStore {
    public final RxDataStore<Preferences> dataStore;

    @Inject
    PrefsDataStore(RxDataStore<Preferences> dataStore){
        this.dataStore = dataStore;
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

    public void saveToken(String token) {
        dataStore.updateDataAsync(preferences -> {
            MutablePreferences _prefs = preferences.toMutablePreferences();
            _prefs.set(KEY_JWT, token);
            return Single.just(_prefs);
        });
    }


    public Flowable<Boolean> getLoginStatus() {
        return dataStore.data().map(preferences -> Optional.ofNullable(preferences.get(KEY_IS_LOGGED_IN)).orElse(false));
    }

    public Flowable<String> getJWTToken() {
        return dataStore.data().map(preferences -> preferences.get(KEY_JWT));
    }
    public static final Preferences.Key<String> KEY_EMAIL = PreferencesKeys.stringKey("key_email");
    public static final Preferences.Key<Boolean> KEY_IS_LOGGED_IN =
            PreferencesKeys.booleanKey("key_is_logged_in");
    public static final Preferences.Key<String> KEY_JWT = PreferencesKeys.stringKey("token");
}
