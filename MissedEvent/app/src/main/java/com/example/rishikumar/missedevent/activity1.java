package com.example.rishikumar.missedevent;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;


public class activity1 extends AppCompatActivity {



    ToggleButton tb;
    int count=0;
    int countsms =0;
    public  SharedPreferences sharedPref;
    boolean button_status= false;
    Button bnc ;
    Button bns ;
    TextView tv2;
    private static final int  REQUEST_PERMISSIONS = 1;
    int unreadMessagesCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity1);
        Log.d("Tagg", "The onCreate() event");




        tb = (ToggleButton) findViewById(R.id.tb1);
        bnc= (Button) findViewById(R.id.bnc);
        bns= (Button) findViewById(R.id.bns);
        tv2 = (TextView) findViewById( R.id.tv2);






        //  addNotification(count);
        sharedPref = getPreferences(Context.MODE_PRIVATE);

       button_status = sharedPref.getBoolean("Button_status", false);
        if(button_status==true){tb.setChecked( true );                       tv2.setText( "Toggle Off to stop the service." );
;            Log.d("tagg", "Button status retrieve as : "+ button_status);
        }
        else{tb.setChecked( false );        Log.d("tagg", "Button status retrieve as : "+ button_status);
        }
        if(button_status==false){
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(002);
            tv2.setText( "Toggle On to start the service." );
;

        }

         bnc.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent( activity1.this , Logs_activity.class );
                    startActivity( i );
                }
            } );

        bns.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 String defaultApplication = Settings.Secure.getString(getContentResolver(), "sms_default_application");
                PackageManager pm = getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage(defaultApplication );
                if (intent != null) {
                    startActivity(intent);
                }

            }
        } );


        tb.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(tb.isChecked()){
                  //  Toast toast = Toast.makeText(getApplicationContext(), "Service has started", Toast.LENGTH_SHORT);
                   // toast.show();
                    button_status= true;
                    startService(view);
                    Log.d("tagg", "Service has been Started");
                    tv2.setText( "Toggle Off to stop the service." );




                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Service has Stopped.", Toast.LENGTH_SHORT);
                    toast.show();
                    button_status = false;
                    stopService(view);
                    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(002);
                    tv2.setText( "Toggle On to start the service." );


                }
            }


        });


    }





    public void startService (View view) {
        startService(new Intent(getBaseContext(), MyService.class));


        }



    public void stopService (View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }




    public void addNotification( int count , int unreadMessagesCount) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("You have "+count+" new missed calls. ")
                        .setContentText("You have "+unreadMessagesCount+" unread messages");

        Intent notificationIntent = new Intent(this, Logs_activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(002, builder.build());
    }





    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tagg", "Activity Resumed");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("tagg", "Activity Restarted");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("tagg", "Activity Stop");
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Button_status", button_status );
        editor.commit();

        Log.d("tagg", "Button status stored as : "+ button_status);

        if(button_status==false){
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(002);

        }

    }






}
