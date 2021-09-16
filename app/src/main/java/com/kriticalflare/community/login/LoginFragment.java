package com.kriticalflare.community.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.MaterialSharedAxis;
import com.kriticalflare.community.AuthenticationViewModel;
import com.kriticalflare.community.R;
import com.kriticalflare.community.databinding.FragmentLoginBinding;
import com.kriticalflare.community.model.LoginUser;
import com.zhuinden.eventemitter.EventSource;

import dev.chrisbanes.insetter.Insetter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class LoginFragment extends Fragment {


    private FragmentLoginBinding binding;
    private AuthenticationViewModel authViewModel;
    private CompositeDisposable compositeDisposable;
    public static final String TAG = "LOGIN_FRAGMENT";
    private EventSource.NotificationToken eventSubscription;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true));
        this.setReenterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, false));
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        Insetter.builder()
                .paddingBottom(WindowInsetsCompat.Type.ime() + WindowInsetsCompat.Type.systemBars(), true)
                .applyToView(binding.loginContainer);
        binding.loginButton.setOnClickListener(view -> {
            if (binding.emailTextfield.getEditText() != null
                    && binding.passwordTextfield.getEditText() != null
            ) {
                String email = binding.emailTextfield.getEditText().getText().toString();
                String password = binding.passwordTextfield.getEditText().getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    authViewModel.login(new LoginUser(email, password));
                } else {
                    makeSnackBar("Check your credentials");
                }
            }
        });
        binding.registerButton.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                authViewModel.isLoggedIn()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean status) {
                                Log.d(TAG, "LOGIN STATUS " + status);
                                if (status) {
                                    Navigation.findNavController(binding.getRoot()).popBackStack(R.id.eventsFragment, false);
                                }
                                request(1);
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
        eventSubscription = authViewModel.eventMessages.startListening(this::makeSnackBar);

    }

    private void makeSnackBar(String message) {
        Snackbar.make(this.binding.rootLayout, message, Snackbar.LENGTH_LONG)
                .setAnchorView(this.binding.spacer)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        compositeDisposable.dispose();
        eventSubscription.stopListening();
    }

}