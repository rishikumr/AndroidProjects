package com.example.rishikumar.missedevent;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;


/**
 * Created by rishi.kumar on 9/12/2017.
 */

public class MyService extends Service {

    View view;
    private Intent intent;
    int count;


    @Nullable


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendOutBroadcastOn( this.view );

        Log.d( "tagg", "Service has started againnnnn( with count ) :" + count + "    " + flags + "    " + startId );


        try {
            if (intent.getStringExtra( "sender" ) != null) {

                count = (intent.getIntExtra( "count", 0 ));
                String sender = intent.getStringExtra( "sender" );

                Log.d( "tagg", "I recieved the intent to Myservice Class  : " + count + "  " + sender );
                addNotification( (count), 0 );
            }
        } catch (NullPointerException e) {

            System.out.print( e.getCause() );
        }


        // :::::::::::::::::::::::::::To count  number of unread messages:::::::::::::::::::::::::::::::::::::::::::
        Cursor c = getContentResolver().query(
                Uri.parse( "content://sms/inbox" ),
                new String[]{
                        "count(_id)",
                },
                "read = 0",
                null,
                null
        );
        c.moveToFirst();
        int unreadMessagesCount = c.getInt( 0 );

        addNotification( count, unreadMessagesCount );


        Log.d( "tagg", "Service has started againnnnn( with count ) :" + count );

        // Let it continue running until it is stopped.
        // onStartCommand(intent, flags,startId);
        //  startForeground( 100, null );
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {


        super.onDestroy();
        Log.d( "tagg", "Service Destroyed" );

        // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        sendOutBroadcastOff( this.view );
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void sendOutBroadcastOn(View view) {
        Intent i = new Intent();

        // i.putExtra("extra","com.example.rishikumar.imagecapture.toggleOn");
        i.setAction( "com.example.rishikumar.misssedevent.toggleOn" );
        i.addFlags( Intent.FLAG_INCLUDE_STOPPED_PACKAGES );
        sendBroadcast( i );
        Log.d( "tagg", "com.example.rishikumar.misssedevent.toggleOn broadcast is sent" );


    }

    private void sendOutBroadcastOff(View view) {
        Intent i = new Intent();
        // i.putExtra("extra","com.example.rishikumar.imagecapture.toggleOn");
        i.setAction( "com.example.rishikumar.misssedevent.toggleOff" );
        i.addFlags( Intent.FLAG_INCLUDE_STOPPED_PACKAGES );
        sendBroadcast( i );
        count = 0;
        Log.d( "tagg", "com.example.rishikumar.misssedevent.toggleOff broadcast is sent" );

    }

    public void addNotification(int count, int unreadMessagesCount) {

        Intent notificationIntent = new Intent( this, activity1.class );
        PendingIntent contentIntent = PendingIntent.getActivity( this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT );
        NotificationManager manager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder( this )
                        .setSmallIcon( R.mipmap.ic_launcher_round )
                        .setContentTitle( "You have " + count + " new missed calls. " )
                        .setContentText( "You have " + unreadMessagesCount + " unread messages" )
                        .setAutoCancel( true )
                        .setLights( Color.parseColor( "#0086dd" ), 2000, 2000 );


        builder.setContentIntent( contentIntent );

        // Add as notification

        manager.notify( 002, builder.build() );
        Log.d( "tagg", "Notificatio has been updated with : " + count );

    }


}

