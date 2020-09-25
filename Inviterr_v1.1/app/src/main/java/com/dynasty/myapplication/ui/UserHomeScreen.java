package com.dynasty.myapplication.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.BuildConfig;
import com.dynasty.myapplication.database.ConnectToServer;
import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;


public class UserHomeScreen extends AppCompatActivity  implements View.OnClickListener {
    public static final String TAG = Constants.LOG_TAG;
    private final String MyPref = BuildConfig.APPLICATION_ID+".SharedPref";
    public final static int SUCCESSFUL_SERVER_RESPONSE = 10;
    public final static int SERVER_ERROR = 11;
    public final static int DELETING_USER_PASS = 18;
    public final static int DELETING_USER_FAIL = 19;
    public final static int GET_USER_PASS = 20;
    public final static int GET_USER_FAIL = 21;


    TextView user_info_home_screen ;
    Button logout_button , delete_account;
    String mUserPhoneNumber;
    String mUserToken;
    String mUserName = "Not found!";
    String mUserPassword;
    String mIpAddress;
    Handler mHandler;
    ProgressBar progressBar;
    Toast toastMessage;
    private String mLastlogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_screen);
        user_info_home_screen = findViewById(R.id.user_info_home_screen);
        logout_button = findViewById(R.id.logout_button);
        delete_account = findViewById(R.id.delete_account);
        progressBar = findViewById(R.id.progressBar_user_profile);
        logout_button.setOnClickListener(this);
        delete_account.setOnClickListener(this);
        user_info_home_screen.setText("Please Wait. Fetching User Details.");

        SharedPreferences pref = getApplicationContext().getSharedPreferences(MyPref, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        mUserToken = pref.getString("mUserToken", null);
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message result){
                progressBar.setVisibility(View.INVISIBLE);
                if(result.what == SUCCESSFUL_SERVER_RESPONSE){
                    onSuccessfulServerResponse((Message) result.obj);
                }else if(result.what == SERVER_ERROR){
                    onServerError((Message) result.obj);
                }else {
                    showToast("Sorry. Unknown Error" );
                    Log.i(TAG, "Sorry. Unknown Error 1") ;
                }
            }
        };

        ConnectToServer mServer = new ConnectToServer(mHandler);
        mServer.doGetUser(mUserToken,UserHomeScreen.this);
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_button:
                Log.i(TAG, "Pressed logout button ");
                onLogOutButtonPressed();
                break;


            case R.id.delete_account:
                Log.i(TAG, "Pressed  Delete button ");
                onDeleteButtonPressed();
                break;
        }
    }

    private void onServerError(Message errorCause) {
        showToast("Server Error : "+ errorCause );
        Log.i(TAG, "Server Error. Please try again!"+ errorCause);
    }

    private void onSuccessfulServerResponse(Message mMessage) {
        switch (mMessage.what) {
            case DELETING_USER_PASS :
                Log.i(TAG, "Deleting user pass.");
                showToast("User deleted successfully" );
                onLogOutButtonPressed();
                break;
            case DELETING_USER_FAIL :
                showToast("User Deletion Error: "+ mMessage.obj );
                Log.i(TAG, "Deleting user fail. \n Error:"+ mMessage.obj);
                break;
            case GET_USER_PASS :
               // showToast("User fetched Successfully" );
                Log.i(TAG, "User Fetch Successfully : "+ mMessage.obj);
                JSONObject userDetailsJsonObj = (JSONObject) mMessage.obj;
                try {
                    mUserPhoneNumber = userDetailsJsonObj.getString("userPhoneNumber");
                    mUserPassword = userDetailsJsonObj.getString("userPassword");
                    mIpAddress =  userDetailsJsonObj.getJSONArray("userIpAddress").toString().replaceAll("[\\[\\](){}]","");
                    mLastlogin = userDetailsJsonObj.getString("lastLogin");
                    updateUserInfo(true , null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case GET_USER_FAIL :
                showToast("User Can not be fetched. Error="+ mMessage.obj );
                Log.i(TAG, "User Fetch Error:"+ mMessage.obj);
                updateUserInfo(false , mMessage.obj);
                break;

            default:
                showToast("Sorry. Unknown Error"    );
                Log.i(TAG, "Sorry. Unknown Error 2") ;

        }
    }

    private void updateUserInfo(boolean fetchedCorrectly , Object message ) {
        if(fetchedCorrectly){
        String mUserInfo = new String(" \t\t\t\t \t\t\t\t\tUser Information \n \nPhoneNumber = " + mUserPhoneNumber)
                + ("\n \nPassword = " + mUserPassword)+ ("\n \n Ip Address = " + mIpAddress) + "\n\n Lastlogin ="+ mLastlogin;
        user_info_home_screen.setText(mUserInfo);
        Log.i(TAG, "mUserInfo Updated to= "+ mUserInfo);
        }else{
            user_info_home_screen.setText(" User Information : \n Sorry. Can not fetch the details now. \n"+ message.toString());
        }
    }




    private void onDeleteButtonPressed() {
        //Toast.makeText(UserHomeScreen.this, "Please wait. Deleting User.", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        ConnectToServer mServer = new ConnectToServer(mHandler);
        mServer.doDelete(mUserPhoneNumber,UserHomeScreen.this);
    }

    private void onLogOutButtonPressed() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(MyPref, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("mUserToken");
        editor.remove("mUserPhoneNumber");
        editor.remove("mUserName");
        editor.remove("mUserPassword");
        editor.remove("mIpAddress");
        editor.commit();
        Toast.makeText(UserHomeScreen.this, "User Logged Out successfully", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon_exit)
                .setTitle("Exit ?")
                .setMessage("Are you sure you want to close this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showToast (String messgae) {
        if(toastMessage != null)  toastMessage.cancel();
        toastMessage = Toast.makeText(UserHomeScreen.this, messgae, Toast.LENGTH_SHORT);
        toastMessage.show();
    }


}