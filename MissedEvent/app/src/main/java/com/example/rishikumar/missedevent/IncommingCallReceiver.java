package com.example.rishikumar.missedevent;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.Date;


/**
 * Created by rishi.kumar on 9/12/2017.
 */
public class IncommingCallReceiver extends BroadcastReceiver
{

    static boolean ring=false;
    static boolean callReceived=false;
    String   callerPhoneNumber;
    static boolean flag = false;
    String from;
    static int count=0;
    DatabaseHelper db;
    Calendar c;

    //private static int lastState = TelephonyManager.CALL_STATE_IDLE;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent)
    {

      // c= Calendar.getInstance();
        db = new DatabaseHelper(context );

       // Log.d("tagg", "broadcast is recieved");

        if (intent.getAction().equals("com.example.rishikumar.misssedevent.toggleOn")) {

        //    Toast.makeText(context, "Broadcast recieved", Toast.LENGTH_LONG).show();
            flag = true;
            Log.d("tagg", "Toggle On broadcast is recieved");


        }
        if (intent.getAction().equals("com.example.rishikumar.misssedevent.toggleOff")) {
         //   Toast.makeText(context, "Broadcast recieving off", Toast.LENGTH_LONG).show();
            count=0;
            flag = false;
            Log.d("tagg", "Toggle Off broadcast is recieved");




        }
        if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            //   Toast.makeText(context, "Broadcast recieving off", Toast.LENGTH_LONG).show();

            flag = false;
            Log.d("tagg", "Power dis-Connected broadcast is recieved");




        }
        if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            //   Toast.makeText(context, "Broadcast recieving off", Toast.LENGTH_LONG).show();

            flag = false;
            Log.d("tagg", "Power Connected broadcast is recieved");




        }
        if ((intent.getAction().equals("android.intent.action.PHONE_STATE")) && flag==true ) {
           // Toast toast = Toast.makeText(context, "You have recieved : "+intent.getAction() , Toast.LENGTH_SHORT);

           //toast.show();

            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            Log.d("tagg", " android.intent.action.PHONE_STATE broadcast is recieved from : " + number);


            // Get the current Phone State
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (state == null)
                return;

            // If phone state "Rininging"
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                ring = true;
                // Get the Caller's Phone Number
                Bundle bundle = intent.getExtras();
                callerPhoneNumber = bundle.getString("incoming_number");
            }


            // If incoming call is received
            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                callReceived = true;
            }


            // If phone is Idle
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
                if (ring == true && callReceived == false) {
                   // Toast.makeText(context, "It was A MISSED CALL from : " + callerPhoneNumber, Toast.LENGTH_LONG).show();
                    count++;
                    from=getContactName(context,number);
                    addlist(number, from);
                    Log.d("tagg", "Missed event has been found  from :"+from    +"   "+number);


                   /* Intent i = new Intent(context, Logs_activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("number",number);
                    context.startActivity(i);
                    Log.d("tagg", "I sent these intent to Logs_Activity Class  :  "+ number);*/



                }
                ring = false;
                callReceived = false;
            }


            Intent intt = new Intent(context,MyService.class);
            intt.putExtra("count",  count );
            intt.putExtra("sender","Not Null Value");
            context.startService(intt);
            Log.d("tagg", "I sent these intent to Myservice Class  : "+ count );
         //   context.startActivity( intt );
         //   Log.d("tagg", "I sent these intent to Activity_1 Class  : "+ count + "  "+ from);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addlist(String number, String from) {
             Log.d("tagg", "I saved : "+from+ number + "  "+   getDateTime());

            db.insertData( from, number , getDateTime().toString() );

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd        HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath( ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex( ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if(contactName==null){return  "Unknown";}
        else{return contactName;}
    }



}