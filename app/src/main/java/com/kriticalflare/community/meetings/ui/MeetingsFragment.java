package com.kriticalflare.community.meetings.ui;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kriticalflare.community.AuthenticationViewModel;
import com.kriticalflare.community.R;
import com.kriticalflare.community.databinding.FragmentMeetingsBinding;

import dagger.hilt.android.AndroidEntryPoint;
import dev.chrisbanes.insetter.Insetter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

@AndroidEntryPoint
public class MeetingsFragment extends Fragment {


    private FragmentMeetingsBinding binding;
    private AuthenticationViewModel authViewModel;
    private MeetingsViewModel meetingsViewModel;
    private CompositeDisposable compositeDisposable;
    private MeetingsAdapter meetingsAdapter;

    public static final String TAG = "MEETINGS_FRAGMENT";

    public MeetingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMeetingsBinding.inflate(inflater, container, false);
        Insetter.builder()
                .paddingBottom(WindowInsetsCompat.Type.navigationBars(), false)
                .applyToView(binding.getRoot());
        TypedValue tv = new TypedValue();
        if (requireContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            binding.meetingsRecycler.setPadding(0, 0, 0, actionBarHeight);
        }
        meetingsAdapter = new MeetingsAdapter();
        binding.meetingsRecycler.setAdapter(meetingsAdapter);
        binding.meetingsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        meetingsViewModel = new ViewModelProvider(this).get(MeetingsViewModel.class);
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
        meetingsViewModel.meetings.observe(getViewLifecycleOwner(), listResource -> {
//            listResource.data.addAll(listResource.data);
            switch (listResource.status) {
                case SUCCESS:
                    meetingsAdapter.submitList(listResource.data);
                    binding.meetingsRecycler.setVisibility(View.VISIBLE);
                    binding.progressIndicator.setVisibility(View.GONE);
                    binding.statusMessage.setVisibility(View.GONE);
                    break;
                case LOADING:
                    binding.meetingsRecycler.setVisibility(View.GONE);
                    binding.progressIndicator.setVisibility(View.VISIBLE);
                    binding.statusMessage.setVisibility(View.GONE);
                    break;
                case ERROR:
                case NO_NETWORK:
                    binding.meetingsRecycler.setVisibility(View.GONE);
                    binding.progressIndicator.setVisibility(View.GONE);
                    binding.statusMessage.setText(listResource.apiMessage);
                    binding.statusMessage.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        compositeDisposable.dispose();
    }
}