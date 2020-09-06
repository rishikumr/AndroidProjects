package com.dynasty.myapplication;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class TestingClass  extends AppCompatActivity implements View.OnClickListener {

    public static String URL_logIn = "http://gauraksha.pythonanywhere.com/api/login01";
    public static String URL_SendOTP = "http://gauraksha.pythonanywhere.com/api/sendotp01";
    public static String URL_SignUp = "http://gauraksha.pythonanywhere.com/api/signup01";
    public static String URL_Delete = "http://gauraksha.pythonanywhere.com/api/deleteuser";

    public static int MY_SOCKET_TIMEOUT_MS = 30000;
    public static final String TAG = Constants.LOG_TAG;
    EditText enterUserIDBox;
    EditText enterPasswordBox;
    EditText enterMobileNumber;
    EditText setMobileNumberBox;
    EditText setPasswordBox;
    EditText enterOTPBox;
    EditText ipAddressBox;
    EditText deleteUserMobile;
    Button logInBox;
    Button receiveOTPBox;
    Button signUpBox;
    Button delete;
    TextView responseBox;
    Button test_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_activity);
        Log.i(TAG, "ONCREATE ");
        enterUserIDBox = findViewById(R.id.enterUserIDBox);
        enterPasswordBox = findViewById(R.id.enterPasswordBox);
        enterMobileNumber = findViewById(R.id.enterMobileNumber);
        setMobileNumberBox = findViewById(R.id.setMobileNumberBox);
        setPasswordBox = findViewById(R.id.setPasswordBox);
        enterOTPBox = findViewById(R.id.enterOTPBox);
        ipAddressBox = findViewById(R.id.ipAddressBox);
        responseBox = findViewById(R.id.responseBox);
        logInBox = findViewById(R.id.logInBox);
        receiveOTPBox = findViewById(R.id.receiveOTPBox);
        signUpBox = findViewById(R.id.signUpBox);
        delete = findViewById(R.id.delete);
        deleteUserMobile = findViewById(R.id.deleteUserMobile);
        test_login = findViewById(R.id.test_login);
        delete.setOnClickListener(this);
        logInBox.setOnClickListener((View.OnClickListener) this);
        receiveOTPBox.setOnClickListener((View.OnClickListener) this);
        signUpBox.setOnClickListener((View.OnClickListener) this);
        test_login.setOnClickListener(this);
        responseBox.setText(R.string.welcomeText);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logInBox:
                doLogIn();
                break;
            case R.id.receiveOTPBox:
                doGetOTP();
                break;
            case R.id.signUpBox:
                doSignUp();
                break;
            case R.id.delete:
                doDelete();
                break;
            case R.id.test_login:
                doTest_login();

        }
    }

    private void doTest_login() {
        Intent intent2 = new Intent(this, EventHomeScreen.class);
        startActivity(intent2);
        finish();

    }

    private void doDelete() {
        responseBox.setText("Deleting USer ");
        RequestQueue requestQueue = Volley.newRequestQueue(TestingClass.this);
        JSONObject payload = new JSONObject();
        String mUsername = deleteUserMobile.getText().toString();
        try {
            payload.put("userPhoneNumber", mUsername);
            Log.i(TAG, "Sending Delete API :: " + payload.toString());

        } catch (JSONException e) {
            Log.i(TAG, "Error setting payload. \n ");
            e.printStackTrace();

        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_Delete, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("success")) {
                            Log.i(TAG, "Success  Deleting  User :: " + response.toString());
                            responseBox.setText(" Success  Deleting  User :: " + response.toString());
                        } else {
                            Log.i(TAG, "Error1 :: " + response.toString());
                            responseBox.setText(" Error1 :: " + response.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error2 :: " + error.getMessage());
                        responseBox.setText(" Error2 : Error logging in \n" + error.getMessage());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }


    private void doLogIn() {
        responseBox.setText(R.string.processingLogIn);
        RequestQueue requestQueue = Volley.newRequestQueue(TestingClass.this);
        JSONObject payload = new JSONObject();
        String mUsername = enterUserIDBox.getText().toString();
        String mPassword = enterPasswordBox.getText().toString();
        try {
            payload.put("userPhoneNumber", mUsername);
            payload.put("userPassword", mPassword);
            Log.i(TAG, "Sending LogIn API :: " + payload.toString());

        } catch (JSONException e) {
            Log.i(TAG, "Error setting payload. \n ");
            e.printStackTrace();

        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_logIn, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("success")) {
                            Log.i(TAG, "Success  logging in :: " + response.toString());
                            String mToken = null;
                            try {
                                JSONObject successJSONobj = response.getJSONObject("success");
                                mToken = successJSONobj.getString("token");
                                Log.i(TAG, "User's token is = " + mToken);
                                responseBox.setText("Success  logging in :: \n    User's token is = " + mToken);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.i(TAG, "Error1 :: " + response.toString());
                            String errorMsg = null;
                            try {
                                JSONObject errJSONobj = response.getJSONObject("error");
                                errorMsg = errJSONobj.getString("errorMsg");
                                responseBox.setText(" Error1 : Error logging in \n" + errorMsg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error2 :: " + error.getMessage());
                        responseBox.setText(" Error2 : Error logging in \n" + error.getMessage());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }


    private void doGetOTP() {
        responseBox.setText(R.string.processingOTPSend);
        RequestQueue requestQueue = Volley.newRequestQueue(TestingClass.this);
        JSONObject payload = new JSONObject();
        String mMobileNumber = enterMobileNumber.getText().toString();
        String mUserIpAddress = ipAddressBox.getText().toString();

        try {
            payload.put("userPhoneNumber", mMobileNumber);
            payload.put("userIpAddress", mUserIpAddress);
            Log.i(TAG, "Sending OTP API:: " + payload.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "Error setting payload.");
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_SendOTP, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("success")) {
                            String mToken = "null";
                            try {
                                JSONObject successJSONobj = response.getJSONObject("success");
                                Log.i(TAG, "Success  logging in :: " + successJSONobj.toString());
                                responseBox.setText("Success sending OTP \n" + successJSONobj.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.i(TAG, "Error1 :: Error sending OTP" + response.toString());
                            String errorMsg = null;
                            try {
                                JSONObject errJSONobj = response.getJSONObject("error");
                                errorMsg = errJSONobj.getString("errorMsg");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            responseBox.setText(" Error1 : Error logging in \n" + errorMsg);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error2 :: " + error);
                        responseBox.setText("Error2: Error sending OTP \n " + error.getMessage());
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }




    private void doSignUp() {
        responseBox.setText(R.string.processingSignUp);
        RequestQueue requestQueue = Volley.newRequestQueue(TestingClass.this);
        JSONObject payload = new JSONObject();
        String mMobileNumber = setMobileNumberBox.getText().toString();
        String mPassword = setPasswordBox.getText().toString();
        String mOTP = enterOTPBox.getText().toString();
        String mUserIpAddress = ipAddressBox.getText().toString();
        try {
            payload.put("userPhoneNumber", mMobileNumber);
            payload.put("userPassword", mPassword);
            payload.put("otp", mOTP);
            payload.put("userIpAddress", mUserIpAddress);
            Log.i(TAG, "Sending SignUp  API:: " + payload.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "Error setting payload.");
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_SignUp, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response.has("success")) {
                            Log.i(TAG, "Success  signing up :: " + response.toString());
                                    String mToken = "null";
                            try {
                                JSONObject successJSONobj = response.getJSONObject("success");
                                mToken = successJSONobj.getString("token");
                                Log.i(TAG, " User's token is = " + mToken);
                                responseBox.setText("Success  signing up :: \n    User's token is = " + mToken);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.i(TAG, "Error1 : Error signing up :: " + response.toString());
                            String errorMsg = null;
                            try {
                                JSONObject errJSONobj = response.getJSONObject("error");
                                errorMsg = errJSONobj.getString("errorMsg");

                                responseBox.setText(" Error1 : Error logging in : " + errorMsg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error :: " + error);
                        responseBox.setText("Error signing up  \n " + error.getMessage());
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

    }
}
