package com.example.rishikumar.m_share;

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by rishi.kumar on 10/17/2017.
 */

public class Songmanager implements Comparable {


    private long id;
    private String title;
    private String artist;
    private String data;
    private Uri albumart;
    private String album;


    public Songmanager(long songID, String songData, String songTitle, String songArtist, String songAlbum, Uri songAlbumArt) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        data = songData;
        album= songAlbum;
        albumart = songAlbumArt;
    }


    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getdata() {
        return data;
    }

    public Uri getAlbumArt() {
        return albumart;
    }
    public String getAlbum() {
        return album;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        String comp = ((Songmanager) o).getTitle();
        return this.getTitle().compareTo(comp);
    }
}
