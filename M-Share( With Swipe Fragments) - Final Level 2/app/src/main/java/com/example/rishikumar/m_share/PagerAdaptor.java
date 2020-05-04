package com.example.rishikumar.m_share;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Switch;

/**
 * Created by rishi.kumar on 11/8/2017.
 */

public class PagerAdaptor extends FragmentPagerAdapter {

    int maxTabs;

    public PagerAdaptor(FragmentManager fm , int maxTabs) {
        super(fm);
        this.maxTabs=maxTabs;

    }




    @Override
    public Fragment getItem(int pos) {

        switch(pos){
            case 0:
                tab1 tab1 = new tab1();
                return tab1;

            case 1:
                tab2 tab2 = new tab2();
                return tab2;


            case 2:
                tab3 tab3 = new tab3();
                return tab3;

        }
        return null;
    }

    @Override
    public int getCount() {
        return maxTabs;
    }
}
