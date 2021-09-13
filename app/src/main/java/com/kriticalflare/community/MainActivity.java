package com.kriticalflare.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kriticalflare.community.databinding.ActivityMainBinding;
import com.kriticalflare.community.databinding.BottomNavSheetBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomNavSheetBinding navSheetBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.Companion.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        navSheetBinding = binding.bttmNav;
        bottomSheetBehavior = BottomSheetBehavior.from(navSheetBinding.bottomNavigationContainer);
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            bottomSheetBehavior.setPeekHeight(actionBarHeight);
        }
        navSheetBinding.navIcon.setOnClickListener(v -> {
           toggleBottomSheet(bottomSheetBehavior);
        });

        navSheetBinding.myToolbar.setOnClickListener(v -> {
           toggleBottomSheet(bottomSheetBehavior);
        });
        setContentView(root);
    }

    private void toggleBottomSheet(BottomSheetBehavior bottomSheetBehavior){
        if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}