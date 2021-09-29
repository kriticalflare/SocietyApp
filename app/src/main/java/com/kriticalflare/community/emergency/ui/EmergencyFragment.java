package com.kriticalflare.community.emergency.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.transition.MaterialSharedAxis;
import com.kriticalflare.community.AuthenticationViewModel;
import com.kriticalflare.community.R;
import com.kriticalflare.community.databinding.FragmentEmergencyBinding;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

@AndroidEntryPoint
public class EmergencyFragment extends Fragment {

    public EmergencyFragment() {
        // Required empty public constructor
    }

    private AuthenticationViewModel authViewModel;
    private CompositeDisposable compositeDisposable;
    private EmergencyViewModel emergencyViewModel;
    private FragmentEmergencyBinding binding;
    private EmergencyAdapter emergencyAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         binding = FragmentEmergencyBinding.inflate(inflater, container,false);
         emergencyAdapter = new EmergencyAdapter();
         binding.emergencyRecycler.setAdapter(emergencyAdapter);
         binding.emergencyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
         return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(authViewModel.isLoggedIn()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableSubscriber<Boolean>() {
            @Override
            public void onNext(Boolean status) {
                if(!status){
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
        }));

        emergencyViewModel = new ViewModelProvider(this).get(EmergencyViewModel.class);

        emergencyViewModel.getEmergencies().observe(getViewLifecycleOwner(), listResource -> {
            switch (listResource.status) {
                case SUCCESS:
                    emergencyAdapter.submitList(listResource.data);
                    binding.emergencyRecycler.setVisibility(View.VISIBLE);
                    binding.progressIndicator.setVisibility(View.GONE);
                    binding.statusMessage.setVisibility(View.GONE);
                    break;
                case LOADING:
                    binding.emergencyRecycler.setVisibility(View.GONE);
                    binding.progressIndicator.setVisibility(View.VISIBLE);
                    binding.statusMessage.setVisibility(View.GONE);
                    break;
                case ERROR:
                case NO_NETWORK:
                    binding.emergencyRecycler.setVisibility(View.GONE);
                    binding.progressIndicator.setVisibility(View.GONE);
                    binding.statusMessage.setText(listResource.apiMessage);
                    binding.statusMessage.setVisibility(View.VISIBLE);
                    break;
            }
        });
        binding.swipeRefresh.setOnRefreshListener(() -> {
            emergencyViewModel.refreshEmergencies();
            Runnable runnable = () -> binding.swipeRefresh.setRefreshing(false);
            binding.swipeRefresh.postDelayed(runnable, 1000);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        compositeDisposable.dispose();
    }
}