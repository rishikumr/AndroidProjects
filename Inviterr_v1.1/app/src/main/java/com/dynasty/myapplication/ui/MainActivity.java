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

}
