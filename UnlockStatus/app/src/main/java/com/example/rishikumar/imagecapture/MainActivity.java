package com.example.rishikumar.imagecapture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    ToggleButton tb;
    TextView tv;
    String str ="::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n\n" +"This app will notify the user whenever screen get unlocked\n\n"+"::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb = (ToggleButton) findViewById(R.id.tb1);
        tv= (TextView) findViewById(R.id.tv);
        tv.setText(str);

        tb.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(tb.isChecked()){
                    Toast toast = Toast.makeText(getApplicationContext(), "Service has started", Toast.LENGTH_SHORT);
                    toast.show();
                    sendOutBroadcastOn(view);

                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Service has Stopped. \nPlease toggle to Start", Toast.LENGTH_SHORT);
                    toast.show();
                    sendOutBroadcastOff(view);
                }
            }


        });



    }






    private void sendOutBroadcastOn(View view) {
        Intent i = new Intent();
       // i.putExtra("extra","com.example.rishikumar.imagecapture.toggleOn");
        i.setAction("com.example.rishikumar.imagecapture.toggleOn");
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(i);
    }
    private void sendOutBroadcastOff(View view) {
        Intent i = new Intent();
        // i.putExtra("extra","com.example.rishikumar.imagecapture.toggleOn");
        i.setAction("com.example.rishikumar.imagecapture.toggleOff");
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(i);
    }


    //                <action android:name="com.example.rishikumar.imagecapture.toggleOn"></action>
    //  <action android:name="android.intent.action.SCREEN_ON" />
      //          <action android:name="android.intent.action.SCREEN_OFF" />
     //           <action android:name="android.intent.action.USER_UNLOCKED" />
}