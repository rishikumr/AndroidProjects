package com.dynasty.myapplication;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class EventRepository {
    private EventDao mEventDao;
    private LiveData<List<Event>> mAllEvents;

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
