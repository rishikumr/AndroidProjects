package com.example.rishikumar.m_share;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rishi.kumar on 11/10/2017.
 */

public class MysongsList {

    private ArrayList<Songmanager> songList;
    private static ArrayList<Songmanager> staticsongList;
    private int i = 0;

   /* public void getSongList() {

        ContentResolver musicResolver = getContentResolver();

        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns

            int dataColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ALBUM);

            int imageColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM_ART);
            Log.d("taggg", "album art  column is original   :" + imageColumn);

            if (imageColumn < 0) {
                imageColumn = 1;
            }


            Log.d("taggg", "album art  column is :" + imageColumn);

            int j = 0;

            //add songs to list
            do {

                long thisId = ++j;
                String thisdata = musicCursor.getString(dataColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);

                Long albumId = musicCursor.getLong(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                // String thisAlbumArt = musicCursor.getString(musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM_ART));

                Log.d("taggg", "You picked with album uri  : " + albumArtUri);
                Songmanager element = new Songmanager(thisId, thisdata, thisTitle, thisArtist, thisAlbum, albumArtUri);
                songList.add(element);
                staticsongList.add(element);

            }
            while (musicCursor.moveToNext());
        }
    }*/
}
