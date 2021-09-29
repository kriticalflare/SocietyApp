package com.kriticalflare.community.parking.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kriticalflare.community.AuthenticationViewModel;
import com.kriticalflare.community.R;
import com.kriticalflare.community.databinding.FragmentScannerBinding;

import dev.chrisbanes.insetter.Insetter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;


public class ScannerFragment extends Fragment {

    private static final String TAG = "SCANNER_FRAGMENT";

    public ScannerFragment() {
        // Required empty public constructor
    }

    private FragmentScannerBinding binding;
    private AuthenticationViewModel authenticationViewModel;
    private CompositeDisposable compositeDisposable;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setFadeMode(MaterialContainerTransform.FADE_MODE_CROSS);
        transform.setScrimColor(Color.TRANSPARENT);
        transform.setPathMotion(new MaterialArcMotion());
        transform.setAllContainerColors(MaterialColors.getColor(requireContext(), R.attr.colorSurface, Color.GRAY));
        setSharedElementEnterTransition(transform);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScannerBinding.inflate(inflater, container, false);
        binding.scanFab.setOnClickListener(v -> {
            IntentIntegrator.forSupportFragment(this)
                    .setOrientationLocked(false)
                    .setBarcodeImageEnabled(true)
                    .setPrompt("Scan a QRCODE")
                    .initiateScan();
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Insetter.builder()
                .padding(WindowInsetsCompat.Type.systemBars())
                .applyToView(binding.getRoot());
        authenticationViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                authenticationViewModel.isLoggedIn()
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                binding.scanResult.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        compositeDisposable.clear();
    }
}