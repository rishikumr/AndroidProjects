package com.dynasty.myapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EventViewModel extends AndroidViewModel {
    private EventRepository mRepository;
    private LiveData<List<Event>> allEvents;
    private LiveData<Event> mEvent;
    private LiveData<Event> imageURis;

    public EventViewModel(@NonNull Application application) {
        super(application);
        mRepository = new EventRepository(application);
        allEvents = mRepository.getAllEvents();

    }

    public void insertEvent(Event mEvent){
        mRepository.insertEvent(mEvent);
    }
    public void updateEvent(Event mEvent){
        mRepository.updateEvent(mEvent);
    }
    public void deleteEvent(Event mEvent){
        mRepository.deleteEvent(mEvent);
    }
    public LiveData<List<Event>> getAllEvents(){
        return  allEvents;
    }
    public LiveData<Event> getEvent (int id) throws ExecutionException, InterruptedException {  return  mRepository.getEvent(id); }
    public void deleteAllEvents(){
        mRepository.deleteAllEvents();
    }

    // Selected Event for Data Passing between Fragments
    public void setSelectedEvent(Event mEvent){
        mRepository.setSelectedEvent(mEvent);
    }
    public Event getSelectedEvent(Event mEvent){
        return mRepository.getSelectedEvent();
    }
}
