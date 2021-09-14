package com.kriticalflare.community;

import androidx.lifecycle.ViewModel;

import com.kriticalflare.community.data.AuthenticationRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;

@HiltViewModel
public class AuthenticationViewModel extends ViewModel {
    private final AuthenticationRepository authRepo;

    @Inject
    AuthenticationViewModel(AuthenticationRepository authRepo) {
        this.authRepo = authRepo;
    }

    public Flowable<Boolean> isLoggedIn() {
        return authRepo.getLoginStatus();
    }

    public void setLoggedIn(boolean status) {
        authRepo.saveLoginStatus(status);
    }
}
