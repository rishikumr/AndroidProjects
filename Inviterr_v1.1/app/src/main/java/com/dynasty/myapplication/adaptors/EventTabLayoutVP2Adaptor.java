package com.dynasty.myapplication.adaptors;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dynasty.myapplication.ui.AllEventsHomeScreen;
import com.dynasty.myapplication.ui.MainFragment;
import com.dynasty.myapplication.ui.MyEventHomeScreen;
import com.dynasty.myapplication.utils.Constants;

public class EventTabLayoutVP2Adaptor extends FragmentStateAdapter {
    public static final String TAG = Constants.LOG_TAG;

    public EventTabLayoutVP2Adaptor(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        Bundle args;
        switch (position){
            case 0 :
                fragment = new AllEventsHomeScreen();
                break;
            case 1:
                fragment = new MyEventHomeScreen();

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fragment;
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
