package com.example.rishikumar.m_share;
/**
 * Created by rishi.kumar on 11/22/2017.
 */

public class UserMangaer {


    private String current_song;
    private String name;
    private String nodeId;
   // private String


    public UserMangaer( String songname , String current_song , String nodeId) {

        name = songname;
        this.current_song= current_song;
        this.nodeId = nodeId;

    }




    public String getcurrent_song() {
        return current_song;
    }

    public String getname() {return name; }

    public String getnodeId() {  return  nodeId ;  }
}
