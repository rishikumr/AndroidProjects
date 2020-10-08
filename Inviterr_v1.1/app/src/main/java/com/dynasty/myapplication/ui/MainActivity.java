package com.dynasty.myapplication.ui;

import android.content.Context;
import android.graphics.Point;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Dimension;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.dynasty.myapplication.R;
import com.dynasty.myapplication.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity{
    public static final String TAG = Constants.LOG_TAG;

    BottomNavigationView bottomNavigationView;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //updateScreenSize();
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        return super.dispatchTouchEvent(ev);
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    protected void updateScreenSize() {
        Display dis =  this.getDisplay();
        Constants.setScreenHeight(dis.getHeight());
        Constants.setScreenWidth(dis.getWidth());
        //Log.d(TAG, dis.getHeight() + "  "+ dis.getWidth()  + (int) (Constants.getScreenWidth() * .002));

    }


    /* TODO:
               Done. 1>  Future and PAST Radio button.
        2> Creater Event :
            a. Fix Image Viwer
            b. Take me to Venue
            b. Implement Invitation Sytem
                i. add contact
                ii. resend reminder icon
                iii. Sort button functionality
            c. "More Details" page : Extra details, organized by, Images
        3> Invitation Event :
            a. remove add button (Mark as for testing only)
            b. remove add & delete button. Replace it with Arrow button.. Accept/Decline events.
            c. Gift Page :
                i. 1st screen uneditable. Your gifts, People gift, Send, Don't send.
                ii. 2ns screen editable. and add gifts.
            d. Images of the event . Add yours.
        4> bottom navigation , Add Icon
        4> Pull out refresh
        5> Side Drawer
    */



}
