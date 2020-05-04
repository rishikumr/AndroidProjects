package com.example.rishikumar.imagecapture;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class BroadReciever extends BroadcastReceiver {

    static boolean flag = false;



    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

       /* if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            Toast toast = Toast.makeText(context, " Screen On  broadcast is recieved (2nd)", Toast.LENGTH_SHORT);
            toast.show();
        }
        else*/
        Log.d("tagg", "broadcast is recieved");

        if (intent.getAction().equals("com.example.rishikumar.imagecapture.toggleOn")) {
            flag = true;
        }
        if (intent.getAction().equals("com.example.rishikumar.imagecapture.toggleOff")) {
            flag = false;



        }
        if ((intent.getAction().equals(Intent.ACTION_USER_PRESENT)) && flag == true) {
            Toast toast = Toast.makeText(context, "You have UNLOCKED the device", Toast.LENGTH_SHORT);

            toast.show();


        }

    }}




