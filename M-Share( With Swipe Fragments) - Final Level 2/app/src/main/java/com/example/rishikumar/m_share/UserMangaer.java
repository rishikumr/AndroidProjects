package com.example.rishikumar.m_share;
/**
 * Created by rishi.kumar on 11/22/2017.
 */

public class UserMangaer {


    private String current_song;
    private String name;


    public UserMangaer( String songname , String current_song) {

        name = songname;
        this.current_song= current_song;

    }
    public UserMangaer( ) {



    }




    public String getcurrent_song() {
        return current_song;
    }

    public String getname() {return name; }


}
