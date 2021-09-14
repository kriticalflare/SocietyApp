package com.kriticalflare.community;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.kriticalflare.community.databinding.ActivityMainBinding;
import com.kriticalflare.community.databinding.BottomNavSheetBinding;

import dagger.hilt.android.AndroidEntryPoint;
import dev.chrisbanes.insetter.Insetter;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private BottomNavSheetBinding navSheetBinding;
    private NavController navController;

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

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(navHostFragment != null){
            navController = navHostFragment.getNavController();
        }

        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()){
                case R.id.eventsFragment:
                    navSheetBinding.navigationView.setCheckedItem(R.id.nav_events);
                    navSheetBinding.myToolbar.setTitle("Events");
                    break;
                case R.id.homeFragment:
                    navSheetBinding.myToolbar.setTitle("Home");
                    break;
                case R.id.loginFragment:
                    navSheetBinding.myToolbar.setTitle("Login");
                    break;
                case R.id.registerFragment:
                    navSheetBinding.myToolbar.setTitle("Register");
                    break;
            }

        });

        navSheetBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_events:
                        navController.popBackStack(R.id.eventsFragment, false);
                        toggleBottomSheet(bottomSheetBehavior);
                        break;
                    case R.id.nav_meetings:
                        navController.navigate(R.id.action_global_homeFragment);
                        toggleBottomSheet(bottomSheetBehavior);
                        break;
                    case R.id.nav_complaints:
                        navController.navigate(R.id.action_global_loginFragment);
                        toggleBottomSheet(bottomSheetBehavior);
                        break;
                    case R.id.nav_parking:
                        navController.navigate(R.id.action_global_registerFragment);
                        toggleBottomSheet(bottomSheetBehavior);
                        break;
                    case R.id.nav_emergency:
                        navController.navigate(R.id.action_global_homeFragment);
                        toggleBottomSheet(bottomSheetBehavior);
                        break;
                }
                return true;
            }
        });

        binding.scrim.setOnClickListener( v -> {
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
                        binding.scrim.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        binding.scrim.setVisibility(View.VISIBLE);
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