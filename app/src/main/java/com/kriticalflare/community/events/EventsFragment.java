package com.kriticalflare.community.events;

import android.os.Bundle;
import android.util.Log;
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
import com.kriticalflare.community.databinding.FragmentEventsBinding;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

@AndroidEntryPoint
public class EventsFragment extends Fragment {


    public static final String TAG = "EVENTS_FRAGMENT";
    private FragmentEventsBinding binding;
    private AuthenticationViewModel authViewModel;
    private EventsViewModel eventsViewModel;
    private CompositeDisposable compositeDisposable;
    private EventsAdapter eventsAdapter;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        eventsAdapter = new EventsAdapter();
        binding.eventsRecycler.setAdapter(eventsAdapter);
        binding.eventsRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);
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
        eventsViewModel.getEvents().observe(getViewLifecycleOwner(), listResource -> {
            switch (listResource.status) {
                case SUCCESS:
                    eventsAdapter.submitList(listResource.data);
                    binding.eventsRecycler.setVisibility(View.VISIBLE);
                    binding.progressIndicator.setVisibility(View.GONE);
                    binding.statusMessage.setVisibility(View.GONE);
                    break;
                case LOADING:
                    binding.eventsRecycler.setVisibility(View.GONE);
                    binding.progressIndicator.setVisibility(View.VISIBLE);
                    binding.statusMessage.setVisibility(View.GONE);
                    break;
                case ERROR:
                case NO_NETWORK:
                    binding.eventsRecycler.setVisibility(View.GONE);
                    binding.progressIndicator.setVisibility(View.GONE);
                    binding.statusMessage.setText(listResource.apiMessage);
                    binding.statusMessage.setVisibility(View.VISIBLE);
                    break;
            }
        });
        binding.swipeRefresh.setOnRefreshListener(() -> {
            eventsViewModel.refreshEvents();
            Runnable runnable = () -> binding.swipeRefresh.setRefreshing(false);
            binding.swipeRefresh.postDelayed(runnable, 1000);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }
}