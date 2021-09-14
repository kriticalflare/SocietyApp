package com.kriticalflare.community;

import androidx.lifecycle.ViewModel;

import com.kriticalflare.community.data.AuthenticationRepository;
import com.kriticalflare.community.model.RegisterUser;
import com.zhuinden.eventemitter.EventSource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;

@HiltViewModel
public class AuthenticationViewModel extends ViewModel {
    private final AuthenticationRepository authRepo;

    public final EventSource<String> eventMessages;

    @Inject
    AuthenticationViewModel(AuthenticationRepository authRepo) {
        this.authRepo = authRepo;
        this.eventMessages = authRepo.errorEvents;
    }

    public void register(RegisterUser user){
        authRepo.register(user);
    }

    public Flowable<Boolean> isLoggedIn() {
        return authRepo.getLoginStatus();
    }

    public void setLoggedIn(boolean status) {
        authRepo.saveLoginStatus(status);
    }
}
