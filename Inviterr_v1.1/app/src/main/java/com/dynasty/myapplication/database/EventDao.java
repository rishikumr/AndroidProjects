package com.dynasty.myapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dynasty.myapplication.entity.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Insert()
    void insert(Event ev);

    @Update()
    void update(Event ev);

    @Delete
    void delete(Event ev);

    @Query("DELETE FROM events_table")
    void deleteAllEvents();

    @Query("SELECT * FROM events_table ORDER BY id  DESC")
    LiveData<List<Event>> getAllEvents();

    @Query("SELECT * FROM events_table WHERE id=:id ")
    LiveData<List<Event>> getImageURIs(int id);

    @Query("SELECT * FROM events_table WHERE id=:id ")
    LiveData<Event> getEvent(int id);
}
