package com.dynasty.myapplication.adaptors;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dynasty.myapplication.ui.*;
import com.dynasty.myapplication.ui.FirstTabEvent.EventInfoScreen;
import com.dynasty.myapplication.ui.SecondTabEvent.GiftManagementScreen;
import com.dynasty.myapplication.ui.SecondTabEvent.InvitationManagementScreen;
import com.dynasty.myapplication.utils.Constants;

public class DetailEvent_TabLayoutVP2Adaptor extends FragmentStateAdapter {
    public static final String TAG = Constants.LOG_TAG;
    boolean isCreator;
    int event_ID;

    public DetailEvent_TabLayoutVP2Adaptor(@NonNull FragmentActivity fragmentActivity, boolean isCreator, int event_ID) {
        super(fragmentActivity);
        this.isCreator = isCreator;
        this.event_ID = event_ID;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment;
        Bundle args;
        switch (position) {
            case 0:
                if (isCreator) {
                    fragment = new EventInfoScreen();
                } else {
                    fragment = new EventInfoScreen();
                }
                args = new Bundle();
                args.putInt(Constants.EXTRA_ID, event_ID);
                args.putInt(Constants.Current_Tab_Position, position);
                fragment.setArguments(args);
                return fragment;
            case 1:
                if (isCreator) {
                    fragment = new InvitationManagementScreen();
                } else {
                    fragment = new GiftManagementScreen();
                }
                args = new Bundle();
                args.putInt(Constants.EXTRA_ID, event_ID);
                args.putInt(Constants.Current_Tab_Position, position);
                fragment.setArguments(args);

                break;
            case 2:
                if (isCreator) {
                    fragment = new EventPhotosCollection();
                } else {
                    fragment = new EventPhotosCollection();
                }
                args = new Bundle();
                args.putInt(Constants.EXTRA_ID, event_ID);
                args.putInt(Constants.Current_Tab_Position, position);
                fragment.setArguments(args);


                args = new Bundle();
                args.putInt(Constants.EXTRA_ID, event_ID);
                args.putInt(Constants.Current_Tab_Position, position);
                fragment.setArguments(args);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);


        }
        return fragment;

    }


    @Override
    public int getItemCount() {
        return 3;
    }
}
