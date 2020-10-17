package com.dynasty.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import android.view.inputmethod.InputMethodManager;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.dynasty.myapplication.R;
import com.dynasty.myapplication.utils.Constants;



public class MainActivity extends AppCompatActivity{
    public static final String TAG = Constants.LOG_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_mainActivity);
        setSupportActionBar(myToolbar);

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

    @Override
    public boolean onSupportNavigateUp() {
        return  Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

/* TODO:
               Done. 1>  Future and PAST Radio button.
        2> Creater Event :
                Done     a. Fix Image Viwer  > Removed VP. FirstImage is set.
            b. Implement Invitation Sytem
                i. add contact
                ii. resend reminder icon
                iii. Sort button functionality
            c. "More Details" page : Extra details, organized by, Images
        3> My Invitation Event :
            a. remove add button (Mark as for testing only)
            b. remove add & delete button. Replace it with Arrow button.. Accept/Decline events.
            c. Gift Page :
                i. 1st screen uneditable. Your gifts, People gift, Send, Don't send.
                ii. 2ns screen editable. and add gifts.
            d. Images of the event . Add yours.
        4> bottom navigation , Add Icon
        4> Pull out refresh
        5> Side Drawer
        6> Add search button
        6> Navigation Fragment issue. No onResume/onPause
        6> Take me to Venue

    */



}
