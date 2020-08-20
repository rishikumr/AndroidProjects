package com.dynasty.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class LogInActivity extends AppCompatActivity implements View.OnClickListener , AdapterView.OnItemSelectedListener{
    public static final String TAG = Constants.LOG_TAG;

    TextView register_button , forgetPassword_button;
    Button login_button_login;
    Toast toastMessage;
    ImageView back_button_log_in;
    EditText enter_phone_number_login;
    EditText enter_password_login;
    Handler mHandler;
    ProgressBar progressBar;
    Spinner spinnerCountryCode;
    String currISDcode;


    public final static int SUCCESSFUL_SERVER_RESPONSE = 10;
    public final static int SERVER_ERROR = 11;
    public final static int LOGGING_PASS = 12;
    public final static int LOGGING_FAIL = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        register_button = findViewById(R.id.register_button_login);
        login_button_login = findViewById(R.id.login_button_login);
        back_button_log_in = findViewById(R.id.back_button_log_in);
        enter_phone_number_login = findViewById(R.id.enter_phone_number_login);
        enter_password_login = findViewById(R.id.enter_password_login);
        progressBar = findViewById(R.id.progressBar_login);
        spinnerCountryCode = findViewById(R.id.countryCodeSpinner_login);
        forgetPassword_button = findViewById(R.id.forgetPassword_button);

        login_button_login.setOnClickListener(this);
        register_button.setOnClickListener(this);
        back_button_log_in.setOnClickListener(this);
        enter_phone_number_login.setOnClickListener(this);
        forgetPassword_button.setOnClickListener(this);

        prepareCountryCodePicker();
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message result){
                progressBar.setVisibility(View.INVISIBLE);
                if(result.what == SUCCESSFUL_SERVER_RESPONSE){
                    onSuccessfulServerResponse((Message) result.obj);
                }else if(result.what == SERVER_ERROR){
                    onServerError((Message) result.obj);
                }else {
                    showToast("Sorry. Unknown Error");
                    Log.i(TAG, "Sorry. Unknown Error 1") ;
                }
            }
        };
    }

    private void prepareCountryCodePicker() {
        spinnerCountryCode.setOnItemSelectedListener(this);
        String[] countriesCode = getResources().getStringArray(R.array.countryCode_Code);
        String[] countriesName = getResources().getStringArray(R.array.countryCode_Names);
        List<String> categories = new ArrayList<String>();
        for(int i = 0 ; i < countriesCode.length ; i++){
            categories.add(countriesName[i] +"  "+ countriesCode[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerCountryCode.setAdapter(dataAdapter);
        spinnerCountryCode.setSelection(0);

    }

    private void onServerError(Message errorCause) {
        showToast("errorCause");
        Log.i(TAG, "Server Error. Please try again!\n"+ errorCause);
    }

    private void onSuccessfulServerResponse(Message mMessage) {
        switch (mMessage.what) {
            case LOGGING_PASS :
                onSuccessLogIn((String)mMessage.obj);
                break;

            case LOGGING_FAIL :
                showToast("Login Failed: "+mMessage.obj );
                Log.i(TAG, "Login Failed : "+ mMessage.obj);
                break;

            default:
                showToast("Sorry. Unknown Error" );
                Log.i(TAG, "Sorry. Unknown Error 2") ;

        }
    }

    private void onSuccessLogIn(String mToken) {
        //UserInformation muser = new UserInformation( enter_phone_number_login.getText().toString(),mToken, enter_password_login.getText().toString());
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MyPref, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("mUserToken", mToken);
        editor.commit();
        Intent intent1 = new Intent(this, EventHomeScreen.class);
        //intent1.putExtra("mOrigin", "LogInActivity");
        //intent1.putExtra("mToken", mToken);
        //intent1.putExtra("mPhoneNumber", enter_phone_number_login.getText().toString());
        finishAffinity();
        startActivity(intent1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button_login:
                Log.i(TAG, "Pressed  LogIn button ");
                //showToast("Please wait. Logging you In." );
                progressBar.setVisibility(View.VISIBLE);
                ConnectToServer mServer = new ConnectToServer(mHandler);
                mServer.doLogIn(currISDcode+enter_phone_number_login.getText().toString(), enter_password_login.getText().toString(), LogInActivity.this);
                break;
            case R.id.forgetPassword_button :
            case R.id.register_button_login:
                Log.i(TAG, "Pressed  register button ");
                Intent intent2 = new Intent(this, RegisterUserActivity.class);
                startActivity(intent2);
                finish();
                break;

            case R.id.back_button_log_in:
                Log.i(TAG, "Pressed  register button ");
                finish();
                break;

        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    private void showToast (String messgae) {
        if(toastMessage != null)    toastMessage.cancel();
        toastMessage = Toast.makeText(LogInActivity.this, messgae, Toast.LENGTH_SHORT);
        toastMessage.show();
    }

    public String getCountryCode(){
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String CountryID= manager.getSimCountryIso().toUpperCase();
        return  CountryID ;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        currISDcode = getResources().getStringArray(R.array.countryCode_Code)[position];
        currISDcode = currISDcode.substring(1);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
