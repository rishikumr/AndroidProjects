package com.example.rishikumar.m_share;


import android.Manifest;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
        , Runnable, MediaPlayer.OnCompletionListener {


    private ArrayList<Songmanager> songList;
    private static ArrayList<Songmanager> staticsongList;
    private Button next, play, prev;
    private static Boolean isPlaying = false, atleatOncePlayed = false, shuffle = false, repeat = false;
    private MediaPlayer mPlayer;
    private int i = 1, p = 0;
    static int j = 1;
    private ImageButton ib1;
    private int max = 0, currentPosition, total;
    private TextView cur, end, tv2, tv3, currentsong;
    private SeekBar skb;
    private ToggleButton tb3, tb4;
    boolean isCompleted = false;
    public static Uri myCurrentUri;

    private ViewPager vp;
    private ConstraintLayout mLayout;
    Window window;

    private SharedPreferences sh;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 200;
    public Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("taggg", "i am on stage " + ++p);
        requestPermissions();
        while ((ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            Log.d("taggg", "i am on stage " + ++p);
        }


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d("taggg", "i am on stage " + ++p);
        songList = new ArrayList<Songmanager>();
        staticsongList = new ArrayList<Songmanager>();
        mLayout = (ConstraintLayout) findViewById(R.id.main_layout);

        TabLayout tblayout = (TabLayout) findViewById(R.id.tabLayout);
        tblayout.addTab(tblayout.newTab().setText("Songs"));
        tblayout.addTab(tblayout.newTab().setText("Playing"));
        tblayout.addTab(tblayout.newTab().setText("Share Now"));


        vp = (ViewPager) findViewById(R.id.pager);
        vp.setOffscreenPageLimit(2);
        PagerAdaptor adaptor = new PagerAdaptor(getSupportFragmentManager(), tblayout.getTabCount());
        vp.setAdapter(adaptor);
        vp.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tblayout));
        vp.setBackgroundResource(R.drawable.background);
        mLayout.setBackgroundResource(R.drawable.background);


        tblayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Log.d("taggg", "i am on stage " + ++p);
        getSongList();
        Log.d("taggg", "i am on stage " + ++p);

        initiate();
        Log.d("taggg", "i am on stage " + ++p);
        try {
            retrieveData();
            Log.d("taggg", "i am on stage " + ++p);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("taggg", "Permission granted ");


                } else {
                    Log.d("taggg", "Permission denied  ");

                    setContentView(R.layout.nopermission);
                    Toast.makeText(this, "To work for this app permission is neccessary \n Provide permission manually", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void songPicked(View view) throws IOException {

        i = (int) view.getTag();
        playMusic(i);
    }


    public static ArrayList<Songmanager> getsongList() {
        return staticsongList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.play:

                if (atleatOncePlayed == true) {

                    playpressed();
                } else playMusic(0);


                break;

            case R.id.prev:
                if (i >= 1) {
                    mPlayer.reset();
                    playMusic(--i);
                }
                break;


            case R.id.next:
                // Log.d("taggg", "You list size is :  " + songList.size());
                if ((i < songList.size() - 1) && shuffle == false) {
                    mPlayer.reset();
                    playMusic(++i);
                } else if (shuffle == true) {

                    mPlayer.reset();
                    Random rd = new Random();
                    i = rd.nextInt(songList.size());
                    playMusic(i);
                }
                break;

            case R.id.currentsong:

                vp.setCurrentItem(1);
                break;


            case R.id.tb3:
                shufflepressed();
                break;

            case R.id.tb4:
                repeatpresssed();
                break;

        }
    }


    public void playpressed() {

        if (isPlaying == true) {

            mPlayer.pause();
            skb.setProgress(currentPosition);

            play.setBackgroundResource(R.drawable.play22);
            tab3.UpdateCurrentPlaying("Online (Not Playing)", false);

            // Log.d("taggg", "You pressed Stop ");
            isPlaying = false;


        } else {
            play.setBackgroundResource(R.drawable.pause22);

            mPlayer.start();
            tab3.UpdateCurrentPlaying(songList.get(i).getTitle(), false);

            isPlaying = true;
            //Log.d("taggg", "You pressed resume ");
        }
    }

    private void repeatpresssed() {
        // Log.d("taggg", "You pressed repeat");
        if (repeat == true) {
            repeat = false;
            tb4.setBackgroundResource(R.drawable.norepeat22);
            tb4.setScaleX((float) 0.8);
            tb4.setScaleY((float) 0.8);
        } else {
            repeat = true;
            tb4.setBackgroundResource(R.drawable.repeat22);
            tb4.setScaleX((float) 0.6);
            tb4.setScaleY((float) 0.6);
        }
    }

    private void shufflepressed() {
        // Log.d("taggg", "You pressed shuffle");
        if (shuffle == true) {
            shuffle = false;
            tb3.setBackgroundResource(R.drawable.noshuffle22);
            tb3.setScaleX((float) 0.6);
            tb3.setScaleY((float) 0.6);
        } else {
            shuffle = true;
            tb3.setBackgroundResource(R.drawable.shuffle22);
            tb3.setScaleX((float) 0.8);
            tb3.setScaleY((float) 0.8);
        }
    }


    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public void playMusic(int i) {
        try {
            j = i;

            myCurrentUri = Uri.parse(songList.get(i).getdata());

            mPlayer.reset();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mPlayer.setDataSource(MainActivity.this, myCurrentUri);

            mPlayer.prepare();

            mPlayer.start();


            tab3.UpdateCurrentPlaying(songList.get(i).getTitle(), false);
            mPlayer.setOnCompletionListener(this);
            isPlaying = true;
            atleatOncePlayed = true;


            play.setBackgroundResource(R.drawable.pause22);


            vp.setCurrentItem(1);
            TextView tv11 = vp.findViewById(R.id.tv1);
            tv2 = vp.findViewById(R.id.tv2);
            tv3 = vp.findViewById(R.id.tv3);
            ib1 = vp.findViewById(R.id.ib1);
            if (tv11 != null) {
                tv11.setText(songList.get(i).getTitle());
            }
            if (tv11 != null) {
                tv3.setText(songList.get(i).getArtist());
            }
            if (tv11 != null) {
                tv2.setText(songList.get(i).getAlbum());
            }


            setStatusColor(Color.rgb(200, 48, 47));
            vp.setBackgroundResource(R.drawable.background);
            mLayout.setBackgroundResource(R.drawable.background);

            String duration = getTimeString(mPlayer.getDuration());
            end.setText(duration);
            max = mPlayer.getDuration();
            skb.setMax(max);
            skb.setProgress(0);
            currentsong.setText(songList.get(i).getTitle());
            UpdateUi task = new UpdateUi();
            task.execute();


            new Thread(this).start();


        } catch (IllegalArgumentException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (SecurityException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (IllegalStateException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

    }




    public class ProgressUpdate {
        public final Uri mUri;
        public final int value;

        public ProgressUpdate(Uri detail, int value) {
            this.mUri = detail;
            this.value = value;
        }

        public Uri getmUri() {
            return mUri;
        }

        public int getIdValue() {
            return value;
        }
    }

    private class UpdateUi extends AsyncTask<Void, ProgressUpdate, String> {

        @Override
        protected String doInBackground(Void... voids) {


            try {
                final Uri coverPath = songList.get(i).getAlbumArt();
                ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(coverPath, "r");
                if (parcelFileDescriptor != null) {
                    Log.d("taggg", "Your selected album art uri is  valid");
                    SharedPreferences.Editor ed = sh.edit();
                    ed.putBoolean("ValidURI", true);
                    ed.commit();
                    //
                    publishProgress(new ProgressUpdate(coverPath, 20));


                    new Thread(new Runnable() {
                        public void run() {
                            Bitmap mBitmmap = null;
                            try {
                                mBitmmap = MediaStore.Images.Media.getBitmap(getContentResolver(), coverPath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (mBitmmap != null) {
                                Log.d("taggg", "Your selected album art bitmap is  valid");
                                Palette.from(mBitmmap).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {

                                        if (palette != null) {
                                            Log.d("taggg", "Your selected album art palette  is  valid");
                                            mLayout.setBackgroundColor(palette.getVibrantColor(Color.rgb(200, 48, 47)));
                                            vp.setBackgroundColor(palette.getVibrantColor(Color.rgb(200, 48, 47)));
                                            // Log.d("taggg", "You got color id :  " +palette.getVibrantColor(Color.rgb(200,48,47)));

                                            setStatusColor(palette.getVibrantColor(Color.rgb(200, 48, 47)));

                                        } else
                                            Log.d("taggg", "Your selected album art palette  is  in valid");
                                    }


                                });
                            } else {
                                Log.d("taggg", "Your selected album art bitmap is invalid");
                            }
                        }
                    }).start();


                } else {
                    Log.d("taggg", "Your selected album art uri is invalid");
                    SharedPreferences.Editor ed = sh.edit();
                    ed.putBoolean("ValidURI", false);
                    ed.commit();


                }


            } catch (Exception e) {
                //  Log.d("taggg", "Your selected album art uri not found");
                SharedPreferences.Editor ed = sh.edit();
                ed.putBoolean("ValidURI", false);
                ed.commit();
                if (ib1 != null) {
                    publishProgress(new ProgressUpdate(Uri.parse("Id"), R.drawable.noalbum1));


                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(ProgressUpdate... values) {
            super.onProgressUpdate(values);
            if (values[0].value != 20) {
                ib1.setImageResource(values[0].value);
            } else if (values[0].value == 20) {

                ib1.setImageURI(values[0].mUri);
            }

        }
    }


    private void setStatusColor(int vibrantColor) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(vibrantColor);
    }


    public void initiate() {
        // songView = (ListView) findViewById(R.id.song_list);


        cur = (TextView) findViewById(R.id.currtime);
        end = (TextView) findViewById(R.id.endtime);

        tb3 = (ToggleButton) findViewById(R.id.tb3);
        tb4 = (ToggleButton) findViewById(R.id.tb4);

        tb3.setOnClickListener(this);
        tb4.setOnClickListener(this);

        next = (Button) findViewById(R.id.next);
        prev = (Button) findViewById(R.id.prev);
        play = (Button) findViewById(R.id.play);
        prev.setBackgroundResource(R.drawable.rewind22);
        play.setBackgroundResource(R.drawable.play22);
        next.setBackgroundResource(R.drawable.forward22);


        next.setOnClickListener(MainActivity.this);
        prev.setOnClickListener(MainActivity.this);
        play.setOnClickListener(MainActivity.this);

        mPlayer = new MediaPlayer();

        currentsong = (TextView) findViewById(R.id.currentsong);
        currentsong.setOnClickListener(this);

        skb = (SeekBar) findViewById(R.id.seekbar);
        skb.setOnSeekBarChangeListener(this);

        sh = getApplicationContext().getSharedPreferences("MyPref22", MODE_PRIVATE);


    }


    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
                /*.append(String.format("%02d", hours))
                .append(":")*/
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    private long getTimeInt(String s) {

        if (s.length() >= 5) {

            String[] data = s.split(":");

            int minutes = Integer.parseInt(data[0]);
            int seconds = Integer.parseInt(data[1]);

            int time = seconds + 60 * minutes;

            //  System.out.println("time in millis = " + TimeUnit.MILLISECONDS.convert(time, TimeUnit.SECONDS));
            return TimeUnit.MILLISECONDS.convert(time, TimeUnit.SECONDS);
        }
        return 0;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        String cuurrr = getTimeString(mPlayer.getCurrentPosition());

        cur.setText(cuurrr);

        if (mPlayer != null && b == true) {
            mPlayer.seekTo(i);
            cuurrr = getTimeString(mPlayer.getCurrentPosition());
            cur.setText(cuurrr);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void run() {
        //  Log.d("taggg", "runnable  has been started.. ");


        currentPosition = mPlayer.getCurrentPosition();
        total = mPlayer.getDuration();

        while (mPlayer != null && currentPosition < total) {
            currentPosition = mPlayer.getCurrentPosition();
            skb.setProgress(currentPosition);
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void getSongList() {


        try {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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

                    int k = 0;

                    //add songs to list
                    do {

                        long thisId = ++k;
                        String thisdata = musicCursor.getString(dataColumn);
                        String thisTitle = musicCursor.getString(titleColumn);
                        String thisArtist = musicCursor.getString(artistColumn);
                        String thisAlbum = musicCursor.getString(albumColumn);

                        Long albumId = musicCursor.getLong(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                        // String thisAlbumArt = musicCursor.getString(musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM_ART));

                        // Log.d("taggg", "You picked with album uri  : " + albumArtUri);
                        Songmanager element = new Songmanager(thisId, thisdata, thisTitle, thisArtist, thisAlbum, albumArtUri);
                        songList.add(element);
                        staticsongList.add(element);

                    }
                    while (musicCursor.moveToNext());
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        Collections.sort(songList);
        Collections.sort(staticsongList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPlayer.reset();


    }

    @Override
    protected void onStop() {
        super.onStop();
        tab3.UpdateCurrentPlaying("Offline", false);
        saveData();


    }

    private void saveData() {

        if (i > 0) {
            SharedPreferences.Editor ed = sh.edit();
            ed.putBoolean("atleatOncePlayed ", atleatOncePlayed);
            ed.putBoolean("shuffle1", !shuffle);
            ed.putBoolean("repeat1", !repeat);
            ed.putInt("i", i);
            ed.putInt("currtime", mPlayer.getCurrentPosition());
            ed.putString("title", songList.get(i).getTitle());
            ed.putString("artist", songList.get(i).getArtist());
            ed.putString("albumm", songList.get(i).getAlbum());
            ed.putString("albummart", songList.get(i).getAlbumArt().toString());
            ed.putString("curtime", getTimeString(mPlayer.getCurrentPosition()));
            ed.putString("totaltime", getTimeString(mPlayer.getDuration()));
            ed.commit();

        }
    }

    private void retrieveData() throws IOException {

        atleatOncePlayed = sh.getBoolean("atleatOncePlayed", false);
        shuffle = sh.getBoolean("shuffle1", true);
        shufflepressed();
        repeat = sh.getBoolean("repeat1", true);
        repeatpresssed();

        i = sh.getInt("i", 0);
        j = i;

        long currmilli = getTimeInt(sh.getString("curtime", "00:00"));
        //  Log.d("taggg", "time reteive as  : " + currmilli);
        currentsong.setText(sh.getString("title", "Playing"));
        cur.setText(sh.getString("curtime", "00:00"));
        end.setText(sh.getString("totaltime", "00:00"));

        try {
            getPausedPlayMusic(i, currmilli);
        } catch (IOException e) {
            e.printStackTrace();
        }
        tab3.UpdateCurrentPlaying("Online (Not Playing)", false);


    }


    private void getPausedPlayMusic(int i, long curtime) throws IOException {
        if (i > 0) {
            j = i;
            Uri myUri = Uri.parse(songList.get(i).getdata());
            mPlayer.reset();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(MainActivity.this, myUri);
            mPlayer.prepare();
            new Thread(this).start();

            mPlayer.seekTo((int) curtime);
            mPlayer.start();
            tab3.UpdateCurrentPlaying(songList.get(i).getTitle(), false);
            mPlayer.setOnCompletionListener(this);

            max = mPlayer.getDuration();
            skb.setMax(max);
            skb.setProgress((int) curtime);
            isPlaying = true;
            atleatOncePlayed = true;

            playpressed();
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        if (vp.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            vp.setCurrentItem(0);
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 500);
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        {
            if ((i < songList.size() - 1) && shuffle == false && repeat == false) {

                playMusic(++i);
            } else if (repeat == true) {

                playMusic(i);
            } else if (shuffle == true && repeat == false) {

                Random rd = new Random();
                i = rd.nextInt(songList.size());
                playMusic(i);
            }
        }
    }


    public static String getCurrentsong() {
        try{if (staticsongList.get(j).getTitle() != null && isPlaying == true)
            return staticsongList.get(j).getTitle();
        else {
            return "Online ( Not Playing )";
        }}
        catch(Exception e){return "Online ( Not Playing )";}
    }


}
