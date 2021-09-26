package com.kriticalflare.community.events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kriticalflare.community.emergency.data.EmergencyRepository;
import com.kriticalflare.community.emergency.data.model.EmergencyItem;
import com.kriticalflare.community.events.data.EventsRepository;
import com.kriticalflare.community.events.data.model.Events;
import com.kriticalflare.community.util.Resource;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EventsViewModel extends ViewModel {
    private EventsRepository repo;

    @Inject
    public EventsViewModel(EventsRepository eventsRepository) {
        this.repo = eventsRepository;
    }

    public LiveData<Resource<List<Events>>> getEvents(){
        return  repo.getEvents();
    }
}
