package com.kriticalflare.community.parking.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kriticalflare.community.parking.data.ParkingRepository;
import com.kriticalflare.community.parking.data.model.ParkingResponse;
import com.kriticalflare.community.util.Resource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ScannerViewModel extends ViewModel {

    private ParkingRepository repo;

    @Inject
    ScannerViewModel(ParkingRepository repo){
        this.repo = repo;
    }

    public LiveData<Resource<ParkingResponse>> claimParkingSpot(String qrCode){
        return repo.claimParkingSpots(qrCode);
    }

    public LiveData<Resource<ParkingResponse>> clearParkingSpot(String qrCode){
        return repo.clearParkingSpot(qrCode);
    }
}
