package com.kriticalflare.community.parking.ui;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.transition.Hold;
import com.google.android.material.transition.MaterialElevationScale;
import com.kriticalflare.community.AuthenticationViewModel;
import com.kriticalflare.community.R;
import com.kriticalflare.community.databinding.FragmentParkingBinding;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class ParkingFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "PARKING_FRAGMENT";

    public ParkingFragment() {
        // Required empty public constructor
    }

    private FragmentParkingBinding binding;
    private GoogleMap map;
    private AuthenticationViewModel authViewModel;
    private CompositeDisposable compositeDisposable;
    private TypedValue tv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setExitTransition(new Hold());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentParkingBinding.inflate(inflater, container, false);
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(this);
        tv = new TypedValue();
        if (requireContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.parkingFab.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, 2 * actionBarHeight);
        }
        binding.parkingFab.setOnClickListener(v -> {
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(binding.parkingFab, "scanner_transform")
                    .build();
            Navigation
                    .findNavController(binding.getRoot())
                    .navigate(
                            R.id.action_parkingFragment_to_scannerFragment,
                            null,
                            null,
                            extras);
        });
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
                                if (!status) {
                                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_loginFragment);
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

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapView.onDestroy();
        binding = null;
        compositeDisposable.dispose();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }
}