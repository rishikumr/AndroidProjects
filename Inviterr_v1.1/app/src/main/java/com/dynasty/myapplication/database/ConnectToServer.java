package com.dynasty.myapplication.database;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dynasty.myapplication.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ConnectToServer  {


    public final static String URL_logIn = "http://gauraksha.pythonanywhere.com/api/login01";
    public final static String URL_SendOTP = "http://gauraksha.pythonanywhere.com/api/sendotp01";
    public final static String URL_SignUp = "http://gauraksha.pythonanywhere.com/api/signup01";
    public final static String URL_DELETE_USER = "http://gauraksha.pythonanywhere.com/api/deleteuser";
    public final static String URL_GET_USER = "http://gauraksha.pythonanywhere.com/api/getuser01";
    public final static int MY_SOCKET_TIMEOUT_MS = 30000;
    public final static int SUCCESSFUL_SERVER_RESPONSE = 10;
    public final static int SERVER_ERROR = 11;
    public final static int LOGGING_PASS = 12;
    public final static int LOGGING_FAIL = 13;
    public final static int SIGNING_PASS = 14;
    public final static int SIGNING_FAIL = 15;
    public final static int SEND_OTP_PASS = 16;
    public final static int SEND_OTP_FAIL = 17;
    public final static int DELETING_USER_PASS = 18;
    public final static int DELETING_USER_FAIL = 19;
    public final static int GET_USER_PASS = 20;
    public final static int GET_USER_FAIL = 21;


    public static String TAG = Constants.LOG_TAG;
    Handler mHandler;




    public ConnectToServer(android.os.Handler mHandler) {
        this.mHandler = mHandler;
    }


    public void doLogIn(String mUsername, String mPassword, Context cx) {
        RequestQueue requestQueue = Volley.newRequestQueue(cx);
        JSONObject payload = new JSONObject();
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
                                Message mMessage = Message.obtain();
                                mMessage.what = LOGGING_PASS;
                                mMessage.obj = mToken;
                                mHandler.obtainMessage(SUCCESSFUL_SERVER_RESPONSE,mMessage).sendToTarget();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.i(TAG, "Error1 :: " + response.toString());
                            String errorMsg = null;
                            try {
                                JSONObject errJSONobj = response.getJSONObject("error");
                                errorMsg = errJSONobj.getString("errorMsg");
                                Message mMessage = Message.obtain();
                                mMessage.what = LOGGING_FAIL;
                                mMessage.obj = errorMsg;
                                mHandler.obtainMessage(SUCCESSFUL_SERVER_RESPONSE,mMessage).sendToTarget();

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
                        Message mMessage = Message.obtain();
                        mMessage.what = SERVER_ERROR;
                        mMessage.obj = error.getMessage();
                        mHandler.obtainMessage(SERVER_ERROR,mMessage).sendToTarget();
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }


    public void doGetOTP(String mMobileNumber , String mUserIpAddress , Context cx) {

        RequestQueue requestQueue = Volley.newRequestQueue(cx);
        JSONObject payload = new JSONObject();

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
                            try {
                                JSONObject successJSONobj = response.getJSONObject("success");
                                Log.i(TAG, "Success  logging in :: " + successJSONobj.toString());
                                Message mMessage = Message.obtain();
                                mMessage.what = SEND_OTP_PASS;
                                mMessage.obj = successJSONobj.toString();
                                mHandler.obtainMessage(SUCCESSFUL_SERVER_RESPONSE,mMessage).sendToTarget();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.i(TAG, "Error1 :: Error sending OTP" + response.toString());
                            String errorMsg = null;
                            try {
                                JSONObject errJSONobj = response.getJSONObject("error");
                                errorMsg = errJSONobj.getString("errorMsg");
                                Message mMessage = Message.obtain();
                                mMessage.what = SEND_OTP_FAIL;
                                mMessage.obj = errorMsg;
                                mHandler.obtainMessage(SUCCESSFUL_SERVER_RESPONSE,mMessage).sendToTarget();
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
                        Message mMessage = Message.obtain();
                        mMessage.what = SERVER_ERROR;
                        mMessage.obj = error.getMessage();
                        mHandler.obtainMessage(SERVER_ERROR,mMessage).sendToTarget();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

    }




    public void doSignUp (String mMobileNumber, String mPassword  , String mOTP , String mUserIpAddress , Context cx) {

        RequestQueue requestQueue = Volley.newRequestQueue(cx);
        JSONObject payload = new JSONObject();
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
                                Log.i(TAG, "User's token is = " + mToken);
                                Message mMessage = Message.obtain();
                                mMessage.what = SIGNING_PASS;
                                mMessage.obj = mToken;
                                mHandler.obtainMessage(SUCCESSFUL_SERVER_RESPONSE,mMessage).sendToTarget();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.i(TAG, "Error1 : Error signing up :: " + response.toString());
                            String errorMsg = null;
                            try {
                                JSONObject errJSONobj = response.getJSONObject("error");
                                errorMsg = errJSONobj.getString("errorMsg");
                                Message mMessage = Message.obtain();
                                mMessage.what = SIGNING_FAIL;
                                mMessage.obj = errorMsg;
                                mHandler.obtainMessage(SUCCESSFUL_SERVER_RESPONSE,mMessage).sendToTarget();


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
                        Message mMessage = Message.obtain();
                        mMessage.what = SERVER_ERROR;
                        mMessage.obj = error.getMessage();
                        mMessage.obj = error.getMessage();
                        mHandler.obtainMessage(SERVER_ERROR,mMessage).sendToTarget();
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);


    }


    public void doDelete(String mPhoneNumber, Context cx) {
        RequestQueue requestQueue = Volley.newRequestQueue(cx);
        JSONObject payload = new JSONObject();
        try {
            payload.put("userPhoneNumber", mPhoneNumber);
            Log.i(TAG, "Sending DELETE USER API :: " + payload.toString());

        } catch (JSONException e) {
            Log.i(TAG, "Error setting payload. \n ");
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_DELETE_USER, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("success")) {
                            Log.i(TAG, "Success  deleting user in :: " + response.toString());
                            Message mMessage = Message.obtain();
                            mMessage.what = DELETING_USER_PASS;
                            mHandler.obtainMessage(SUCCESSFUL_SERVER_RESPONSE,mMessage).sendToTarget();

                        } else {
                            Log.i(TAG, "Error1 :: " + response.toString());
                            String errorMsg = null;
                            try {
                                JSONObject errJSONobj = response.getJSONObject("error");
                                errorMsg = errJSONobj.getString("errorMsg");
                                Message mMessage = Message.obtain();
                                mMessage.what = DELETING_USER_FAIL;
                                mMessage.obj = errorMsg;
                                mHandler.obtainMessage(SUCCESSFUL_SERVER_RESPONSE,mMessage).sendToTarget();

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
                        Message mMessage = Message.obtain();
                        mMessage.what = SERVER_ERROR;
                        mMessage.obj = error.getMessage();
                        mHandler.obtainMessage(SERVER_ERROR,mMessage).sendToTarget();
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

    }


    public void doGetUser(final String mToken, Context cx) {
        final RequestQueue requestQueue = Volley.newRequestQueue(cx);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_USER, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("success")) {
                            JSONObject userJSONobj= null;
                            try {
                                JSONObject successJSONobj = response.getJSONObject("success");
                                userJSONobj = successJSONobj.getJSONObject("USERS");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i(TAG, "Success  Getting  USER info :: " + userJSONobj.toString());
                            Message mMessage = Message.obtain();
                            mMessage.what = GET_USER_PASS;
                            mMessage.obj = userJSONobj;
                            mHandler.obtainMessage(SUCCESSFUL_SERVER_RESPONSE,mMessage).sendToTarget();

                        } else {
                            Log.i(TAG, "Error1 :: " + response.toString());
                            String errorMsg = null;
                            try {
                                JSONObject errJSONobj = response.getJSONObject("error");
                                errorMsg = errJSONobj.getString("errorMsg");
                                Message mMessage = Message.obtain();
                                mMessage.what = GET_USER_FAIL;
                                mMessage.obj = errorMsg;
                                mHandler.obtainMessage(SUCCESSFUL_SERVER_RESPONSE,mMessage).sendToTarget();

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
                        Message mMessage = Message.obtain();
                        mMessage.what = SERVER_ERROR;
                        mMessage.obj = error.getMessage();
                        mHandler.obtainMessage(SERVER_ERROR,mMessage).sendToTarget();
                    }
                })
        { // Notice no semi-colon here
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("auth", mToken);
                Log.i(TAG, "token to send for fetch user = " + mToken);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

    }
}
