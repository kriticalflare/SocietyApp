package com.kriticalflare.community.meetings.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kriticalflare.community.meetings.data.MeetingsRepository;
import com.kriticalflare.community.meetings.data.model.Meeting;
import com.kriticalflare.community.util.Resource;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MeetingsViewModel extends ViewModel {
    private MeetingsRepository repo;
    public LiveData<Resource<List<Meeting>>> meetings;

    @Inject
    public MeetingsViewModel(MeetingsRepository repo) {
        this.repo = repo;
        meetings = repo.getMeetings();
    }
}
