package com.dynasty.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnKeyListener {
    public static final String TAG = Constants.LOG_TAG;

    EditText enter_phone_number, enter_name, enter_password, phone_number_afterotp;
    EditText editOTPDigit1, editOTPDigit2, editOTPDigit3, editOTPDigit4, editOTPDigit5;
    Button sign_up_button, receiveOTP_signUp;
    ImageView back_button_sign_up;
    Handler mHandler;
    String currIPAddress;
    Toast toastMessage;
    String currPhoneNumber;
    ProgressBar progressBar;
    Spinner spinnerCountryCode;
    String currISDCode, currOTP;
    public final static int SUCCESSFUL_SERVER_RESPONSE = 10;
    public final static int SERVER_ERROR = 11;
    public final static int SIGNING_PASS = 14;
    public final static int SIGNING_FAIL = 15;
    public final static int SEND_OTP_PASS = 16;
    public final static int SEND_OTP_FAIL = 17;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_activity_for_otp);
        enter_phone_number = findViewById(R.id.enter_phone_number_afterotp);
        receiveOTP_signUp = findViewById(R.id.receiveOTP_signUp);
        progressBar = findViewById(R.id.progressBar_cyclic_signup_otp);
        back_button_sign_up = findViewById(R.id.back_button_sign_up);
        receiveOTP_signUp.setOnClickListener(this);
        back_button_sign_up.setOnClickListener(this);

        prepareCountryCodePicker();
       // updateUI("+91 9990715249");


                mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message result) {
                progressBar.setVisibility(View.INVISIBLE);
                if (result.what == SUCCESSFUL_SERVER_RESPONSE) {
                    onSuccessfulServerResponse((Message) result.obj);
                } else if (result.what == SERVER_ERROR) {
                    onServerError(result);
                } else {
                    showToast("Sorry. Unknown Error");
                    Log.i(TAG, "Sorry. Unknown Error 1");
                }
            }
        };

        currIPAddress = getLocalIpAddress();
        Log.i(TAG, "currIPAddress =" + currIPAddress);
    }

    private void prepareCountryCodePicker() {
        spinnerCountryCode = findViewById(R.id.countryCodeSpinner_signin);
        spinnerCountryCode.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        String[] countriesCode = getResources().getStringArray(R.array.countryCode_Code);
        String[] countriesName = getResources().getStringArray(R.array.countryCode_Names);
        List<String> categories = new ArrayList<String>();
        for (int i = 0; i < countriesCode.length; i++) {
            categories.add(countriesName[i] + "  " + countriesCode[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerCountryCode.setAdapter(dataAdapter);
        spinnerCountryCode.setSelection(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.receiveOTP_signUp:
                Log.i(TAG, "Pressed  sign_up_button ");
                progressBar.setVisibility(View.VISIBLE);
                currPhoneNumber = enter_phone_number.getText().toString();
                ConnectToServer mServer = new ConnectToServer(mHandler);
                mServer.doGetOTP(currISDCode + enter_phone_number.getText().toString(), currIPAddress, RegisterUserActivity.this);
                // FOR test
                //updateUI(currPhoneNumber);
                break;
            case R.id.sign_up_button:
                Log.i(TAG, "Pressed  register button ");
                //showToast("Please wait. Signing you In." );
                progressBar.setVisibility(View.VISIBLE);
                enter_password = findViewById(R.id.enter_password_signup);
                ConnectToServer mServer1 = new ConnectToServer(mHandler);
                mServer1.doSignUp(currISDCode+phone_number_afterotp.getText().toString(), enter_password.getText().toString(), getOTPfromUI(), currIPAddress, RegisterUserActivity.this);
                break;

            case R.id.back_button_sign_up:
                Log.i(TAG, "Pressed  back button ");
                finish();
                break;
        }
    }

    private String getOTPfromUI() {
        currOTP = editOTPDigit1.getText().toString() + editOTPDigit2.getText().toString() + editOTPDigit3.getText().toString() + editOTPDigit4.getText().toString() + editOTPDigit5.getText().toString();
        if (currOTP.length() != 5) {
            showToast("Please input full OTP.");
        }
        return currOTP;
    }

    private void updateUI(String currPhoneNumber) {

        setContentView(R.layout.register_user_activity);
        sign_up_button = findViewById(R.id.sign_up_button);
        sign_up_button.setOnClickListener(this);
        back_button_sign_up = findViewById(R.id.back_button_sign_up);
        back_button_sign_up.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar_cyclic_signup);
        phone_number_afterotp = findViewById(R.id.enter_phone_number_afterotp);
        phone_number_afterotp.setText(currPhoneNumber);
        phone_number_afterotp.setFocusable(false);
        editOTPDigit1 = findViewById(R.id.editOTPDigit1);
        editOTPDigit2 = findViewById(R.id.editOTPDigit2);
        editOTPDigit3 = findViewById(R.id.editOTPDigit3);
        editOTPDigit4 = findViewById(R.id.editOTPDigit4);
        editOTPDigit5 = findViewById(R.id.editOTPDigit5);
        editOTPDigit1.addTextChangedListener(new GenericTextWatcher(editOTPDigit1));
        editOTPDigit2.addTextChangedListener(new GenericTextWatcher(editOTPDigit2));
        editOTPDigit3.addTextChangedListener(new GenericTextWatcher(editOTPDigit3));
        editOTPDigit4.addTextChangedListener(new GenericTextWatcher(editOTPDigit4));
        editOTPDigit5.addTextChangedListener(new GenericTextWatcher(editOTPDigit5));

        editOTPDigit1.setOnKeyListener(this);
        editOTPDigit2.setOnKeyListener(this);
        editOTPDigit3.setOnKeyListener(this);
        editOTPDigit4.setOnKeyListener(this);
        editOTPDigit5.setOnKeyListener(this);

        editOTPDigit5.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    editOTPDigit5.clearFocus();

                    return true;
                }
                return false;
            }

        });
    }



    private void onServerError(Message errorCause) {
        showToast("Server Error : " + errorCause);
        Log.i(TAG, "Server Error. Please try again!" + errorCause);
    }

    private void onSuccessfulServerResponse(Message mMessage) {
        switch (mMessage.what) {
            case SEND_OTP_PASS:
                Log.i(TAG, "Send OTP pass : " + mMessage.obj);
                onSendOTPSuccess((String) mMessage.obj);
                break;
            case SEND_OTP_FAIL:
                showToast("OTP sent error : " + mMessage.obj);
                Log.i(TAG, "Send OTP fail : " + mMessage.obj);
                break;
            case SIGNING_PASS:
                Log.i(TAG, "SignIn Passed : " + mMessage.obj);
                onSuccessSignIn((String) mMessage.obj);
                break;
            case SIGNING_FAIL:
                showToast("SignIn Failed: " + mMessage.obj);
                Log.i(TAG, "SignIn Failed : " + mMessage.obj);
                break;

            default:
                showToast("Sorry. Unknown Error");
                Log.i(TAG, "Sorry. Unknown Error 2");

        }
    }

    private void onSendOTPSuccess(String mMessage) {
        showToast("OTP " + mMessage);
        updateUI(currPhoneNumber);
    }

    private void onSuccessSignIn(String mToken) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MyPref, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("mUserToken", mToken);
        editor.putString("mUserPhoneNumber", enter_phone_number.getText().toString());
        editor.putString("mUserPassword", enter_password.getText().toString());
        editor.putString("mIpAddress", currIPAddress);
        editor.commit();
        Log.i(TAG, "User logged in. "+ mToken);
        Intent intent = new Intent(this, EventHomeScreen.class);
        finishAffinity();
        startActivity(intent);
    }

    public static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        return  addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
    public String getLocalIpAddress() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String curIP= Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        if(curIP.equals("0.0.0.0")){
            return getMobileIPAddress().substring(0,25);
        }
        return curIP;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void showToast(String messgae) {
        if (toastMessage != null) toastMessage.cancel();
        toastMessage = Toast.makeText(RegisterUserActivity.this, messgae, Toast.LENGTH_SHORT);
        toastMessage.show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        currISDCode = getResources().getStringArray(R.array.countryCode_Code)[position];
        currISDCode = currISDCode.substring(1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    int lastDeleted;

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        EditText ed = (EditText) view;
        Editable text = ed.getText();
        Log.d(TAG, "onKey: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN ) {
            switch (view.getId()) {
                case R.id.editOTPDigit1:
                    lastDeleted= 1;
                    break;
                case R.id.editOTPDigit2:
                    if(lastDeleted == 2 ){
                        editOTPDigit1.requestFocus();
                    }
                    lastDeleted=2;
                    break;
                case R.id.editOTPDigit3:
                    if(lastDeleted == 3 ){
                        editOTPDigit2.requestFocus();
                    }
                    lastDeleted=3;
                    break;
                case R.id.editOTPDigit4:
                    if(lastDeleted == 4 ){
                        editOTPDigit3.requestFocus();
                    }
                    lastDeleted=4;
                    break;
                case R.id.editOTPDigit5:
                    if(lastDeleted == 5 ){
                        editOTPDigit4.requestFocus();
                    }
                    lastDeleted=5;
                    break;
            }
        }



        return false;
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.d(TAG, "onTextChanged: charSequence=" + charSequence + i + i1 + i2);
        }

        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            if (text.length() == 0) return;
            switch (view.getId()) {
                case R.id.editOTPDigit1:

                    editOTPDigit2.requestFocus();

                    break;
                case R.id.editOTPDigit2:

                    editOTPDigit3.requestFocus();


                    break;
                case R.id.editOTPDigit3:

                    editOTPDigit4.requestFocus();

                    break;
                case R.id.editOTPDigit4:

                    editOTPDigit5.requestFocus();
                    editOTPDigit5.setImeOptions(EditorInfo.IME_ACTION_DONE);

                    break;
                case R.id.editOTPDigit5:
                    editOTPDigit5.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    break;


            }
        }
    }




}
