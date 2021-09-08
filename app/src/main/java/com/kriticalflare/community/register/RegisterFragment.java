package com.kriticalflare.community.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.MaterialSharedAxis;
import com.kriticalflare.community.R;
import com.kriticalflare.community.databinding.FragmentLoginBinding;
import com.kriticalflare.community.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

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
        binding.registerButton.setOnClickListener(view -> {
            if (binding.usernameTextfield.getEditText() != null && binding.passwordTextfield.getEditText() != null) {
                if (binding.usernameTextfield.getEditText().getText().toString().equals("krithik")
                        && binding.passwordTextfield.getEditText().getText().toString().equals("12345678")) {
//                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
                } else {
                    Snackbar.make(this.binding.rootLayout, "Check your credentials", Snackbar.LENGTH_LONG)
                            .show();
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}