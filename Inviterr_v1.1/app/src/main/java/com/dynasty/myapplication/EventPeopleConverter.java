package com.dynasty.myapplication;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventPeopleConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<People> stringToPeopleList(String data) {
        if (data == null) {
            return (ArrayList)Collections.emptyList();
        }

        Type listType = new TypeToken<ArrayList<People>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String PeopleListToString(ArrayList<People> someObjects) {
        return gson.toJson(someObjects);
    }

}
