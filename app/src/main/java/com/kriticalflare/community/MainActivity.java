package com.kriticalflare.community;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kriticalflare.community.databinding.ActivityMainBinding;
import com.kriticalflare.community.databinding.BottomNavSheetBinding;

import dev.chrisbanes.insetter.Insetter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private BottomNavSheetBinding navSheetBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen.Companion.installSplashScreen(this);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        navSheetBinding = binding.bttmNav;
        View root = binding.getRoot();

        Insetter.builder()
                .margin(WindowInsetsCompat.Type.statusBars())
                .applyToView(root);

        Insetter.builder()
                .padding(WindowInsetsCompat.Type.navigationBars())
                .applyToView(navSheetBinding.bottomNavigationContainer);
        Insetter.builder()
                .paddingBottom(WindowInsetsCompat.Type.navigationBars(),true)
                .applyToView(navSheetBinding.myToolbar);

        bottomSheetBehavior = BottomSheetBehavior.from(navSheetBinding.bottomNavigationContainer);
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            bottomSheetBehavior.setPeekHeight(actionBarHeight);
        }

        navSheetBinding.navIcon.setOnClickListener(v -> {
            toggleBottomSheet(bottomSheetBehavior);
        });

        navSheetBinding.myToolbar.setOnClickListener(v -> {
            toggleBottomSheet(bottomSheetBehavior);
        });

        OnBackPressedCallback closeBottomSheetCallback = new OnBackPressedCallback(
                false
        ) {
            @Override
            public void handleOnBackPressed() {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        };
        getOnBackPressedDispatcher().addCallback(
                this, // LifecycleOwner
                closeBottomSheetCallback);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        closeBottomSheetCallback.setEnabled(false);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        closeBottomSheetCallback.setEnabled(true);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        setContentView(root);
    }

    private void toggleBottomSheet(BottomSheetBehavior<View> bottomSheetBehavior) {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}