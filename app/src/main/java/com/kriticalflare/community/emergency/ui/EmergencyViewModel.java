package com.kriticalflare.community.emergency.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kriticalflare.community.emergency.data.EmergencyRepository;
import com.kriticalflare.community.emergency.data.model.EmergencyItem;
import com.kriticalflare.community.util.Resource;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EmergencyViewModel extends ViewModel {

    private EmergencyRepository repo;

    @Inject
    public EmergencyViewModel(EmergencyRepository emergencyRepository) {
        this.repo = emergencyRepository;
    }

    public LiveData<Resource<List<EmergencyItem>>> getEmergencies(){
        return  repo.getEmergencies();
    }
}
