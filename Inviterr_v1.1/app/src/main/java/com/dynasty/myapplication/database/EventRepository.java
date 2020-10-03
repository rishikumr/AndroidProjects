package com.dynasty.myapplication.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.dynasty.myapplication.database.EventDao;
import com.dynasty.myapplication.database.EventDataBase;
import com.dynasty.myapplication.entity.Event;
import com.dynasty.myapplication.entity.People;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EventRepository {
    private EventDao mEventDao;
    private LiveData<List<Event>> mAllEvents;
    private ArrayList<String> giveGifts= new ArrayList<>();
    private ArrayList<String> doNotGiveGifts= new ArrayList<>();
    private ArrayList<String> myGivenGifts= new ArrayList<>();
    private ArrayList<People> guestInvitationList= new ArrayList<>();


    // Current selected Event for Data Passing between Frags
    private Event currSelectedEvent;

    public EventRepository(Application app) {
        EventDataBase mEventDataBase = EventDataBase.getInstance(app);
        mEventDao = mEventDataBase.EventDao();
        mAllEvents = mEventDao.getAllEvents();
    }

    public  void insertEvent(Event mEvent){
        new insertEventInDBAsyncTask(mEventDao).execute(mEvent);
    }

    public  void updateEvent (Event mEvent){
        new updateEventInDBAsyncTask(mEventDao).execute(mEvent);
    }

    public  void deleteEvent (Event mEvent){
        new deleteEventInDBAsyncTask(mEventDao).execute(mEvent);
    }

    public LiveData<Event> getEvent (int id) throws ExecutionException, InterruptedException {
       return new getEventInDBAsyncTask(mEventDao).execute(id).get();
    }

    public void deleteAllEvents(){
        new deleteAllEventsInDBAsyncTask(mEventDao).execute();
    }

    public LiveData<List<Event>> getAllEvents(){
        return mAllEvents;
    }



    // Current selected Event for Data Passing between Frags
    public Event getSelectedEvent() {        return currSelectedEvent;    }
    public void setSelectedEvent(Event currSelectedEvent) {        this.currSelectedEvent = currSelectedEvent;    }

    public ArrayList<String> getGiveGifts(int id) {
        giveGifts = new ArrayList<>();

        giveGifts.add("Washing Machine");giveGifts.add("Bike");giveGifts.add("Television");giveGifts.add("Kindle");
        giveGifts.add("4 wheeler");giveGifts.add("Pet animal");
        return giveGifts;
    }

    public ArrayList<String> getEventDoNotGiveGifts(int id) {
        doNotGiveGifts = new ArrayList<>();
        doNotGiveGifts.add("Basket" );doNotGiveGifts.add("Radio");doNotGiveGifts.add("Phone");
        doNotGiveGifts.add("Photo Album" );doNotGiveGifts.add("Kitchen Set");doNotGiveGifts.add("Mixer");
        doNotGiveGifts.add("Flowers");



        return doNotGiveGifts;
    }

    public ArrayList<String> getMyGivenGifts(int currEventID) {
        myGivenGifts = new ArrayList<>();
        myGivenGifts.add("Cash");myGivenGifts.add("Washing Machine");myGivenGifts.add("Towels");myGivenGifts.add("Tea Set");myGivenGifts.add("Music system");
        return myGivenGifts;
    }

    public ArrayList<People> getCurrentGuestInvitationList() {
        return guestInvitationList = new People().getDummyPeopleList();
    }






    private static class insertEventInDBAsyncTask extends AsyncTask<Event ,Void, Void >{
        EventDao mDao;
        public insertEventInDBAsyncTask(EventDao mEventDao) {
            this.mDao = mEventDao;
        }

        @Override
        protected Void doInBackground(Event... events) {
            mDao.insert(events[0]);
            return null ;
        }
    }

    private class updateEventInDBAsyncTask extends AsyncTask<Event , Void , Void>{
        EventDao mDao;
        public updateEventInDBAsyncTask(EventDao mEventDao) {
            this.mDao = mEventDao;
        }
        @Override
        protected Void doInBackground(Event... events) {
            mDao.update(events[0]);
            return null;
        }
    }

    private class deleteEventInDBAsyncTask extends AsyncTask<Event , Void , Void>{
        EventDao mDao;
        public deleteEventInDBAsyncTask(EventDao mEventDao) {
            this.mDao = mEventDao;
        }

        @Override
        protected Void doInBackground(Event... events) {
            mDao.delete(events[0]);
            return null;
        }
    }

    private class deleteAllEventsInDBAsyncTask extends  AsyncTask<Void , Void , Void>{
        EventDao mDao;
        public deleteAllEventsInDBAsyncTask(EventDao mEventDao) {
            this.mDao = mEventDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
           // Log.d(Constants.LOG_TAG, "deleteAllEventsInDBAsyncTask ");
            mDao.deleteAllEvents();
            return null;
        }
    }



    private static class getEventInDBAsyncTask extends AsyncTask<Integer, Void,  LiveData<Event> > {
        EventDao mDao;
        public getEventInDBAsyncTask(EventDao mEventDao) {
            this.mDao = mEventDao;
        }

        @Override
        protected  LiveData<Event> doInBackground(Integer... ints) {
           return   mDao.getEvent(ints[0]);
           
        }
    }


}
