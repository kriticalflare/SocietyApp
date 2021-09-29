package com.kriticalflare.community.parking.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kriticalflare.community.parking.data.ParkingRepository;
import com.kriticalflare.community.parking.data.model.GetParkingResponse;
import com.kriticalflare.community.util.Resource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ParkingViewModel extends ViewModel {

    private ParkingRepository repo;

    @Inject
    ParkingViewModel(ParkingRepository repo){
        this.repo = repo;
    }

    public LiveData<Resource<GetParkingResponse>> getParkingSpots(){
        return repo.getParkingSpots();
    }
}
