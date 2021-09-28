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
import androidx.lifecycle.ViewModelProvider;
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
import dev.chrisbanes.insetter.OnApplyInsetsListener;
import dev.chrisbanes.insetter.ViewState;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private BottomNavSheetBinding navSheetBinding;
    private NavController navController;
    private AuthenticationViewModel authViewModel;

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

        authViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);

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
                    setSheetVisible(true);
                    navSheetBinding.navigationView.setCheckedItem(R.id.nav_events);
                    navSheetBinding.myToolbar.setTitle("Events");
                    break;
                case R.id.emergencyFragment:
                    setSheetVisible(true);
                    navSheetBinding.navigationView.setCheckedItem(R.id.nav_emergency);
                    navSheetBinding.myToolbar.setTitle("Emergency");
                    break;
                case R.id.parkingFragment:
                    setSheetVisible(true);
                    navSheetBinding.navigationView.setCheckedItem(R.id.nav_parking);
                    navSheetBinding.myToolbar.setTitle("Parking");
                    break;
                case R.id.scannerFragment:
                    setSheetVisible(false);
                    navSheetBinding.navigationView.setCheckedItem(R.id.nav_none);
                    break;
                case R.id.homeFragment:
                    setSheetVisible(true);
                    navSheetBinding.myToolbar.setTitle("Home");
                    break;
                case R.id.meetingsFragment:
                    setSheetVisible(true);
                    navSheetBinding.navigationView.setCheckedItem(R.id.nav_meetings);
                    navSheetBinding.myToolbar.setTitle("Meetings");
                    break;
                case R.id.loginFragment:
                case R.id.registerFragment:
                    setSheetVisible(false);
                    navSheetBinding.navigationView.setCheckedItem(R.id.nav_none);
                    break;
            }

        });

        navSheetBinding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_meetings:
                    navController.popBackStack(R.id.meetingsFragment, false);
                    break;
                case R.id.nav_events:
                    navController.navigate(R.id.action_global_eventsFragment);
                    break;
                case R.id.nav_complaints:
                    navController.navigate(R.id.action_global_homeFragment);
                    break;
                case R.id.nav_parking:
                    navController.navigate(R.id.action_global_parkingFragment);
                    break;
                case R.id.nav_emergency:
                    navController.navigate(R.id.action_global_emergencyFragment);
                    break;
                case R.id.nav_logout:
                    authViewModel.logout();
                    navSheetBinding.navigationView.setCheckedItem(R.id.nav_none);
                    break;
            }
            toggleBottomSheet(bottomSheetBehavior);
            return true;
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

    private void setSheetVisible(boolean visiblity){
        if(visiblity){
            if(navSheetBinding.bottomNavigationContainer.getVisibility() != View.VISIBLE){
                navSheetBinding.bottomNavigationContainer.setVisibility(View.VISIBLE);
            }
        } else {
            if(navSheetBinding.bottomNavigationContainer.getVisibility() != View.GONE){
                navSheetBinding.bottomNavigationContainer.setVisibility(View.GONE);
            }
        }
    }

    private void toggleBottomSheet(BottomSheetBehavior<View> bottomSheetBehavior) {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}