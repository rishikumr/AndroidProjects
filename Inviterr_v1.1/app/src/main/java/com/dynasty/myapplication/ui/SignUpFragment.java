package com.dynasty.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.R;
import com.dynasty.myapplication.database.ConnectToServer;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener, View.OnKeyListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = Constants.LOG_TAG;

    EditText enter_password ,phone_number_after_otp;
    EditText editOTPDigit1, editOTPDigit2, editOTPDigit3, editOTPDigit4, editOTPDigit5;
    Button sign_up_button;
    ImageView back_button_sign_up;
    Handler mHandler;
    String currIPAddress;
    Toast toastMessage;
    String currPhoneNumber;
    ProgressBar progressBar;
    String currISDCode, currOTP;
    NavController navController;
    public final static int SUCCESSFUL_SERVER_RESPONSE = 10;
    public final static int SERVER_ERROR = 11;
    public final static int SIGNING_PASS = 14;
    public final static int SIGNING_FAIL = 15;
    public final static int SEND_OTP_PASS = 16;
    public final static int SEND_OTP_FAIL = 17;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            currPhoneNumber = getArguments().getString("mMobileNumber");
            currISDCode = getArguments().getString("mISDCode");
        }
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        sign_up_button = requireActivity().findViewById(R.id.sign_up_button);
        sign_up_button.setOnClickListener(this);
        back_button_sign_up = requireActivity().findViewById(R.id.back_button_sign_up);
        back_button_sign_up.setOnClickListener(this);
        progressBar =requireActivity().findViewById(R.id.progressBar_cyclic_signup);
        phone_number_after_otp = requireActivity().findViewById(R.id.phone_number_after_otp);
        if(currPhoneNumber.length()  >1 ) phone_number_after_otp.setText(("+"+currISDCode +"-"+ currPhoneNumber ));
        phone_number_after_otp.setFocusable(false);

        editOTPDigit1 = requireActivity().findViewById(R.id.editOTPDigit1);
        editOTPDigit2 = requireActivity().findViewById(R.id.editOTPDigit2);
        editOTPDigit3 = requireActivity().findViewById(R.id.editOTPDigit3);
        editOTPDigit4 = requireActivity().findViewById(R.id.editOTPDigit4);
        editOTPDigit5 = requireActivity().findViewById(R.id.editOTPDigit5);
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
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(Objects.requireNonNull(requireActivity().getCurrentFocus()).getWindowToken(), 0);
                    editOTPDigit5.clearFocus();
                    return true;
                }
                return false;
            }

        });

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

    private void onServerError(Message errorCause) {
        showToast("Server Error : " + errorCause);
        Log.i(TAG, "Server Error. Please try again!" + errorCause);
    }

    private void onSuccessfulServerResponse(Message mMessage) {
        switch (mMessage.what) {

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

    private void onSuccessSignIn(String mToken) {

        SharedPreferences pref = requireActivity().getApplicationContext().getSharedPreferences(Constants.MyPref, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("mUserToken", mToken);
        editor.putString("mUserPhoneNumber", currPhoneNumber);
        editor.putString("mUserPassword", enter_password.getText().toString());
        editor.putString("mIpAddress", currIPAddress);
        editor.apply();
        Log.i(TAG, "User logged in. "+ mToken);
        navController.navigate(R.id.action_signUpFragment_to_homeScreenTabHost);

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
        WifiManager wm = (WifiManager) requireActivity().getSystemService(Context.WIFI_SERVICE);
        String curIP = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        if(curIP.equals("0.0.0.0")){
            return getMobileIPAddress().substring(0,25);
        }
        return curIP;

    }

    private void showToast(String messgae) {
        if (toastMessage != null) toastMessage.cancel();
        toastMessage = Toast.makeText(requireActivity(), messgae, Toast.LENGTH_SHORT);
        toastMessage.show();
    }

    private String getOTPFromUI() {
        currOTP = editOTPDigit1.getText().toString() + editOTPDigit2.getText().toString() + editOTPDigit3.getText().toString() + editOTPDigit4.getText().toString() + editOTPDigit5.getText().toString();
        if (currOTP.length() != 5) {
            showToast("Please input full OTP.");
        }
        return currOTP;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sign_up_button:
                Log.i(TAG, "Pressed  register button " + currIPAddress);
                //showToast("Please wait. Signing you In." );
                progressBar.setVisibility(View.VISIBLE);
                enter_password = requireActivity().findViewById(R.id.enter_password_signup);
                ConnectToServer mServer1 = new ConnectToServer(mHandler);
                mServer1.doSignUp(currISDCode+ currPhoneNumber, enter_password.getText().toString(), getOTPFromUI(), currIPAddress, requireActivity());
                break;

            case R.id.back_button_sign_up:
                Log.i(TAG, "Pressed  back button ");
                navController.navigateUp();
                break;
        }
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