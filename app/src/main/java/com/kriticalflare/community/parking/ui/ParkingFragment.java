package com.kriticalflare.community.parking.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.transition.Hold;
import com.google.android.material.transition.MaterialSharedAxis;
import com.kriticalflare.community.AuthenticationViewModel;
import com.kriticalflare.community.R;
import com.kriticalflare.community.databinding.FragmentParkingBinding;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

@AndroidEntryPoint
public class ParkingFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "PARKING_FRAGMENT";

    public ParkingFragment() {
        // Required empty public constructor
    }

    private FragmentParkingBinding binding;
    private GoogleMap map;
    private AuthenticationViewModel authViewModel;
    private ParkingViewModel parkingViewModel;
    private CompositeDisposable compositeDisposable;
    private TypedValue tv;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentParkingBinding.inflate(inflater, container, false);
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
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
            setExitTransition(new Hold());
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
        parkingViewModel = new ViewModelProvider(this).get(ParkingViewModel.class);
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private void getLastLocation() {
        if (isLocationPermGranted()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        if (location != null) {
                            LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());
                            map.addMarker(new MarkerOptions()
                                    .position(userPosition)
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                    .title("Current Location"))
                                    .showInfoWindow();
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 15));
                        } else {
                            Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(requireContext(), "Please turn on your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(requireContext(), "Grant Location permission to continue", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private boolean isLocationPermGranted() {
        return (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                } else {
                    Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                }
            });

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
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
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        getLastLocation();
        parkingViewModel.getParkingSpots().observe(getViewLifecycleOwner(), parkingResource -> {
            switch (parkingResource.status) {
                case SUCCESS:
                    if (parkingResource.data != null) {
                        parkingResource.data.getUserParking().forEach(userParkingItem -> {
                            LatLng position = new LatLng(Double.parseDouble(userParkingItem.getLatitude()), Double.parseDouble(userParkingItem.getLongitude()));
                            map.addMarker(new MarkerOptions()
                                    .position(position)
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                                    ).title("Your Car"));
                        });
                        parkingResource.data.getOtherParking().forEach(otherParkingItem -> {
                            LatLng position = new LatLng(Double.parseDouble(otherParkingItem.getLatitude()), Double.parseDouble(otherParkingItem.getLongitude()));
                            map.addMarker(new MarkerOptions()
                                    .position(position)
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(
                                                    otherParkingItem.getUser() != null
                                                            ? BitmapDescriptorFactory.HUE_RED
                                                            : BitmapDescriptorFactory.HUE_BLUE
                                            )
                                    ).title(otherParkingItem.getUser() != null ? "Occupied" : "Free: " + otherParkingItem.getQrCode()));
                        });
                    }
                    break;
                case ERROR:
                case LOADING:
                case NO_NETWORK:
                    break;
            }
        });
    }
}