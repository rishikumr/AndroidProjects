package com.example.rishikumar.m_share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rishi.kumar on 11/22/2017.
 */

public class UserListAdaptor extends BaseAdapter {

    private ArrayList<UserMangaer> users;
    private LayoutInflater ListInf;

    public UserListAdaptor(Context c, ArrayList<UserMangaer> theSongs){
        users=theSongs;
        ListInf=LayoutInflater.from(c);
    }


    @Override
    public int getCount() {

        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout songLay = (LinearLayout)ListInf.inflate
                (R.layout.userlist, parent, false);
        //get title and artist views
        TextView ProfileName = (TextView)songLay.findViewById(R.id.profile_name);
        TextView Current_Song = (TextView)songLay.findViewById(R.id.current_song);
        //get song using position
        UserMangaer currSong = users.get(position);
        //get title and artist strings
        ProfileName.setText("   "+ currSong.getname());
        Current_Song.setText(" : "+ "  " +currSong.getcurrent_song());
        //set position as tag
        songLay.setTag(position);
        return songLay;
    }


}
