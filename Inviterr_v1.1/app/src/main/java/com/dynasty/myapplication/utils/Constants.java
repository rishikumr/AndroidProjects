package com.dynasty.myapplication.utils;

import android.app.Application;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;

import android.text.BoringLayout;
import android.util.DisplayMetrics;
import com.android.volley.BuildConfig;

import java.io.File;
import java.util.ArrayList;

public class Constants {

    public static final String MyPref = BuildConfig.APPLICATION_ID+".SharedPref";
    public static final String TABLE_NAME = "events_table";
    public static final String LOG_TAG= "Inviterr:";
    public static final int VIEW_EVENT_DETAILS = 1;
    public static final int EDIT_EVENT_DETAILS = 2;
    public static final int ADD_EVENT_DETAILS = 3;
    public static final String EXTRA_NAME = "extra_event_name";
    public static final String EXTRA_LOCATION = "extra_event_location";
    public static final String EXTRA_DATE = "extra_event_date";
    public static final String EXTRA_DESC = "extra_event_desc";
    public static final String EXTRA_ID = "extra_event_id";
    public static final String EXTRA_IMAGE_URIS = "extra_event_image_uris";
    public static final Object APP_NAME = "Inviterr";
    public static final String  APP_DIRECTORY =  Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
    public static String  HomeScreen_Tab1_Name = "My Events";
    public static String  HomeScreen_Tab2_Name = "Invitations";
    public static String EventDetailScreen_Tab1_Name = "Event";
    public static String EventDetailScreen_Tab2_1_Name = "Gifts";
    public static String EventDetailScreen_Tab2_2_Name = "Invitation";
    public static String EventDetailScreen_Tab3_1Name = "Photos";
    public static String EventDetailScreen_Tab3_2Name = "More Details";

    public static String IS_CREATOR = "IsCreator";
    public static String Current_Tab_Position = "current_tab_position";

    public static int  screenWidth ;
    public static int  screenHeight  ;

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        Constants.screenWidth = screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        Constants.screenHeight = screenHeight;
    }




    public static ArrayList<String> getDummyEventImages1() {

        ArrayList<String> dummyImgURIs = new ArrayList<>();
        dummyImgURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/ev2").toString());
        dummyImgURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/ev3").toString());
        dummyImgURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/ev4").toString());
        return dummyImgURIs;
    }
    public static ArrayList<String> getDummyEventImages2() {

        ArrayList<String> dummyImgURIs = new ArrayList<>();
        dummyImgURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/ev3").toString());
        dummyImgURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/ev4").toString());
        dummyImgURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/ev2").toString());
        return dummyImgURIs;
    }
    public static ArrayList<String> getDummyEventImages3() {

        ArrayList<String> dummyImgURIs = new ArrayList<>();

        dummyImgURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/ev4").toString());
        dummyImgURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/ev3").toString());
        dummyImgURIs.add(Uri.parse("android.resource://com.dynasty.myapplication/drawable/ev2").toString());
        return dummyImgURIs;
    }






}
