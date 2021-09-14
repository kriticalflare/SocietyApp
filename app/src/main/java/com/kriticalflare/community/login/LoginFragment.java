package com.kriticalflare.community.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.MaterialSharedAxis;
import com.kriticalflare.community.AuthenticationViewModel;
import com.kriticalflare.community.R;
import com.kriticalflare.community.databinding.FragmentLoginBinding;

import dev.chrisbanes.insetter.Insetter;
import dev.chrisbanes.insetter.OnApplyInsetsListener;
import dev.chrisbanes.insetter.ViewState;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class LoginFragment extends Fragment {


    private FragmentLoginBinding binding;
    private String username;
    private String password;
    private AuthenticationViewModel authViewModel;
    private CompositeDisposable compositeDisposable;

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
            if (binding.usernameTextfield.getEditText() != null && binding.passwordTextfield.getEditText() != null) {
                if (binding.usernameTextfield.getEditText().getText().toString().equals("krithik")
                        && binding.passwordTextfield.getEditText().getText().toString().equals("12345678")) {
                    authViewModel.setLoggedIn(true);
//                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
                } else {
                    Snackbar.make(this.binding.rootLayout, "Check your credentials", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
        binding.registerButton.setOnClickListener( view -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}