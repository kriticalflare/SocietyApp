package com.kriticalflare.community.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.MaterialSharedAxis;
import com.kriticalflare.community.AuthenticationViewModel;
import com.kriticalflare.community.databinding.FragmentRegisterBinding;
import com.kriticalflare.community.model.RegisterUser;
import com.zhuinden.eventemitter.EventSource;

import dev.chrisbanes.insetter.Insetter;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private AuthenticationViewModel authViewModel;
    private EventSource.NotificationToken eventSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true));
        this.setReturnTransition(new MaterialSharedAxis(MaterialSharedAxis.X, false));
        this.setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true));
        this.setReenterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, false));
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        Insetter.builder()
                .paddingBottom(WindowInsetsCompat.Type.ime() + WindowInsetsCompat.Type.systemBars(), true)
                .applyToView(binding.registerContainer);

        binding.registerButton.setOnClickListener(view -> {
            if (binding.emailTextField.getEditText() != null
                    && binding.passwordTextfield.getEditText() != null
                    && binding.roomNumberTextfield.getEditText() != null
                    && binding.buildingNumberTextfield.getEditText() != null) {
                String email = binding.emailTextField.getEditText().getText().toString();
                String password = binding.passwordTextfield.getEditText().getText().toString();
                String roomNumber = binding.roomNumberTextfield.getEditText().getText().toString();
                String buildingNumber = binding.buildingNumberTextfield.getEditText().getText().toString();

                if (!email.isEmpty()
                        && !password.isEmpty()
                        && !roomNumber.isEmpty()
                        && !buildingNumber.isEmpty()) {
                    RegisterUser user = new RegisterUser(email, password, roomNumber, buildingNumber);
                    authViewModel.register(user);
                } else {
                    makeSnackBar("Please enter all details");
                }
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.postponeEnterTransition();
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);

        eventSubscription =  authViewModel.eventMessages.startListening(this::makeSnackBar);
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
        eventSubscription.stopListening();
    }
}