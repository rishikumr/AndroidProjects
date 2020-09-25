package com.dynasty.myapplication.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventImagesConverter {

    static Gson gson = new Gson();
    @TypeConverter
    public static ArrayList<String> stringToImageURIList(String data) {
        if (data == null) {
            return (ArrayList) Collections.emptyList();
        }

        Type listType = new TypeToken<ArrayList<String>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ImageURIListToString(ArrayList<String> someObjects) {
        return gson.toJson(someObjects);
    }
}
