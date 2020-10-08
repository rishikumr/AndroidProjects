package com.dynasty.myapplication.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.entity.Event;
import com.dynasty.myapplication.utils.EventPeopleConverter;
import com.dynasty.myapplication.entity.People;

@Database(entities =  {Event.class}, version=1)
@TypeConverters({EventPeopleConverter.class})
public abstract class EventDataBase extends RoomDatabase {
    public static EventDataBase instance;
    public abstract EventDao EventDao();
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
            mDao.insert(new Event("Conference Meeting", "SELF" , "10-05-2020", "Delhi", "Please don't forget to attend this high level meting you have to be in.\nPlease mark your availability",Constants.getDummyEventImages1() ,new People().getDummyPeopleList()));
            mDao.insert( new Event("Birthday Celebration", "Sam", "12-05-2022", "Bangalore", "Hey ! It is my birthday celebration party. \n Come join us and celebrate together", Constants.getDummyEventImages3(),new People().getDummyPeopleList()));
            mDao.insert(new Event("Celebration Meeting", "SELF", "11-05-2022",  "Mumbai", "Come together on this joyous occasion ", Constants.getDummyEventImages2() ,  new People().getDummyPeopleList()));
            mDao.insert(new Event("Family dinner", "Pam", "13-05-2020",  "Hydrabad", "Please join us to have a dinner with all of your loved ones.", Constants.getDummyEventImages3(), new People().getDummyPeopleList()));
            return null;
        }
    }





}
