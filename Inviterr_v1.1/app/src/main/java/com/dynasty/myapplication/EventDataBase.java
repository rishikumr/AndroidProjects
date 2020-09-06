package com.dynasty.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;

@Database(entities =  {Event.class}, version=1)
@TypeConverters({EventPeopleConverter.class})
public abstract class EventDataBase extends RoomDatabase {
    public static EventDataBase instance;
    public abstract  EventDao EventDao();
    public static synchronized  EventDataBase getInstance(Context cn){
        if(instance == null){
            instance = Room.databaseBuilder(cn.getApplicationContext(),
                    EventDataBase.class , "event_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d(Constants.LOG_TAG, "onCreate:  Callback");
            new PopulateDummyEventsAsyncTask(instance).execute();
        }
    };

    public  static class PopulateDummyEventsAsyncTask extends AsyncTask<Void ,Void , Void> {
        private  EventDao  mDao;
        public PopulateDummyEventsAsyncTask(EventDataBase db) {
            mDao = db.EventDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(Constants.LOG_TAG, "doInBackground:  Preparing dummy List");
            mDao.insert(new Event("Conference Meeting", "10-05-2020", "Delhi", "Please don't forget to attend this high level meting you have to be in.\nPlease mark your availability",Constants.getDummyEventImages1() ,new People().getDummyPeopleList()));
            return null;
        }
    }





}
