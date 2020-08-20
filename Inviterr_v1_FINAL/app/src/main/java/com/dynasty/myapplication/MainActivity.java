package com.dynasty.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.BuildConfig;


public class MainActivity extends AppCompatActivity     implements View.OnClickListener {


    public static final String TAG = Constants.LOG_TAG;


    Button testing_button;
    Button login_button;
    TextView register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MyPref, 0); // 0 - for private mode
        String currToken = pref.getString("mUserToken", null);
        Log.i(TAG, " MainActivity : currToken="+ currToken);

        if(currToken != null){
            Intent intent1 = new Intent(this, EventHomeScreen.class);
            intent1.putExtra("mOrigin", "MainActivity");
            intent1.putExtra("mToken", currToken);
            startActivity(intent1);
            finish();
        }else{
            setContentView(R.layout.activity_main);
            Log.i(TAG, "ON CREATE MainActivity");
            testing_button = findViewById(R.id.testing_button);
            login_button = findViewById(R.id.login_button);
            register_button = findViewById(R.id.register_button);

            testing_button.setOnClickListener( this);
            login_button.setOnClickListener( this);
            register_button.setOnClickListener( this);
        }



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testing_button:
                Log.i(TAG, "Pressed  testing button ");
                Intent intent1 = new Intent(this, TestingClass.class);
                startActivity(intent1);
                break;
            case R.id.login_button:
                Log.i(TAG, "Pressed  login button ");
                Intent intent2 = new Intent(this, LogInActivity.class);
                startActivity(intent2);
                break;
            case R.id.register_button:
                Log.i(TAG, "Pressed  register button ");
                Intent intent3 = new Intent(this, RegisterUserActivity.class);
                startActivity(intent3);
                break;

        }
    }




}
