package com.dynasty.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dynasty.myapplication.database.EventRepository;
import com.dynasty.myapplication.entity.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EventViewModel extends AndroidViewModel {
    private EventRepository mRepository;
    private LiveData<List<Event>> allEvents;
    private LiveData<Event> mEvent;
    private LiveData<Event> imageURis;
    private ArrayList<String> giveGifts;
    private ArrayList<String> doNotGiveGifts;
    private ArrayList<String> myGivenGifts;

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

    public ArrayList<String> getEventGiveGifts(int id) {
        giveGifts = mRepository.getGiveGifts(id);
        return giveGifts;
    }

    public ArrayList<String> getEventDoNotGiveGifts(int id) {
        doNotGiveGifts = mRepository.getEventDoNotGiveGifts(id);
        return doNotGiveGifts;
    }

    public ArrayList<String> getMyGivenGifts(int currEventID) {
        myGivenGifts = mRepository.getMyGivenGifts(currEventID);
        return myGivenGifts;
    }
}
